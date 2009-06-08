-- Copyright (c) 1991-2002, The Numerical Algorithms Group Ltd.
-- All rights reserved.
-- Copyright (C) 2007-2009, Gabriel Dos Reis.
-- All rights reserved.
--
-- Redistribution and use in source and binary forms, with or without
-- modification, are permitted provided that the following conditions are
-- met:
--
--     - Redistributions of source code must retain the above copyright
--       notice, this list of conditions and the following disclaimer.
--
--     - Redistributions in binary form must reproduce the above copyright
--       notice, this list of conditions and the following disclaimer in
--       the documentation and/or other materials provided with the
--       distribution.
--
--     - Neither the name of The Numerical Algorithms Group Ltd. nor the
--       names of its contributors may be used to endorse or promote products
--       derived from this software without specific prior written permission.
--
-- THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
-- IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
-- TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
-- PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
-- OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
-- EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
-- PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
-- PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
-- LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
-- NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
-- SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


import msgdb
import pathname
import modemap
import define
namespace BOOT

module compiler where
  coerce: (%Triple,%Mode) -> %Maybe %Triple
  convert: (%Triple,%Mode) -> %Maybe %Triple
  comp: (%Form,%Mode,%Env) -> %Maybe %Triple
  compOrCroak: (%Form,%Mode,%Env) -> %Maybe %Triple
  compCompilerPredicate: (%Form,%Env) -> %Maybe %Triple
  checkCallingConvention: (%List,%Short) -> %SimpleArray %Short


--% 
compUniquely: (%Form,%Mode,%Env) -> %Maybe %Triple
compNoStacking: (%Form,%Mode,%Env) -> %Maybe %Triple
compNoStacking1: (%Form,%Mode,%Env,%List) -> %Maybe %Triple
compOrCroak1: (%Form,%Mode,%Env,%Thing) -> %Maybe %Triple
comp2: (%Form,%Mode,%Env) -> %Maybe %Triple
comp3: (%Form,%Mode,%Env) -> %Maybe %Triple
compExpression: (%Form,%Mode,%Env) -> %Maybe %Triple
compAtom: (%Form,%Mode,%Env) -> %Maybe %Triple
compSymbol: (%Form,%Mode,%Env) -> %Maybe %Triple
compString: (%Form,%Mode,%Env) -> %Maybe %Triple
compAtomWithModemap: (%Form,%Mode,%Env,%Thing) -> %Maybe %Triple
compTypeOf: (%Form,%Mode,%Env) -> %Maybe %Triple
compForm: (%Form,%Mode,%Env) -> %Maybe %Triple
compForm1: (%Form,%Mode,%Env) -> %Maybe %Triple
compForm2: (%Form,%Mode,%Env,%List) -> %Maybe %Triple
compForm3: (%Form,%Mode,%Env,%List) -> %Maybe %Triple
compArgumentsAndTryAgain: (%Form,%Mode,%Env) -> %Maybe %Triple
compExpressionList: (%List,%Mode,%Env) -> %Maybe %Triple
compWithMappingMode: (%Form,%Mode,%List) -> %List
compFormMatch: (%Modemap,%List) -> %Boolean
compFormWithModemap: (%Form,%Mode,%Env,%Modemap) -> %Maybe %Triple
compToApply: (%Form,%List,%Mode,%Env) -> %Maybe %Triple
compApplication: (%Form,%List,%Mode,%Triple) -> %Maybe %Triple

primitiveType: %Thing -> %Mode
modeEqual: (%Form,%Form) -> %Boolean
hasUniqueCaseView: (%Form,%Mode,%Env) -> %Boolean
convertOrCroak: (%Triple,%Mode) -> %Maybe %Triple
getFormModemaps: (%Form,%Env) -> %List
transImplementation: (%Form,%Modemap,%Thing) -> %Code
reshapeArgumentList: (%Form,%Signature) -> %Form
applyMapping: (%Form,%Mode,%Env,%List) -> %Maybe %Triple


++ A list of routines for diagnostic reports.  These functions, in an
++ abstract sense, have type: forall T: Type . String -> T, so they
++ can be used in T-returning functions, for any T.  
$coreDiagnosticFunctions == 
  '(error userError systemError)

$IOFormDomains == 
  [$InputForm,$OutputForm,$Syntax]

++ list of functions to compile
$compileOnlyCertainItems := []


--%

compTopLevel: (%Form,%Mode,%Env) -> %Maybe %Triple
compTopLevel(x,m,e) ==
  -- signals that target is derived from lhs-- see NRTmakeSlot1Info
  $NRTderivedTargetIfTrue: local := false
  $killOptimizeIfTrue: local:= false
  $forceAdd: local:= false
  -- start with a base list of domains we may inline.
  $optimizableConstructorNames: local := $SystemInlinableConstructorNames
  x is ["DEF",:.] or x is ["where",["DEF",:.],:.] =>
    ([val,mode,.]:= compOrCroak(x,m,e); [val,mode,e])
        --keep old environment after top level function defs
  compOrCroak(x,m,e)

compUniquely(x,m,e) ==
  $compUniquelyIfTrue: local:= true
  CATCH("compUniquely",comp(x,m,e))

compOrCroak(x,m,e) == 
  compOrCroak1(x,m,e,'comp)

compOrCroak1(x,m,e,compFn) ==
  fn(x,m,e,nil,nil,compFn) where
    fn(x,m,e,$compStack,$compErrorMessageStack,compFn) ==
      T:= CATCH("compOrCroak",FUNCALL(compFn,x,m,e)) => T
      --stackAndThrow here and moan in UT LISP K does the appropriate THROW
      $compStack:= [[x,m,e,$exitModeStack],:$compStack]
      $s: local :=
        compactify $compStack where
          compactify al ==
            null al => nil
            LASSOC(first first al,rest al) => compactify rest al
            [first al,:compactify rest al]
      $level: local := #$s
      errorMessage:=
        $compErrorMessageStack ^= nil => first $compErrorMessageStack
        "unspecified error"
      $scanIfTrue =>
        stackSemanticError(errorMessage,mkErrorExpr $level)
        ["failedCompilation",m,e]
      displaySemanticErrors()
      SAY("****** comp fails at level ",$level," with expression: ******")
      displayComp $level
      userError errorMessage

++ The form `x' is intended to be evaluated by the compiler, e.g. in
++ toplevel conditional definition or as sub-domain predicate.  
++ Normalize operators and compile the form.
compCompilerPredicate(x,e) ==
  $normalizeTree: local := true
  compOrCroak(parseTran x, $Boolean, e)

comp(x,m,e) ==
  T:= compNoStacking(x,m,e) => ($compStack:= nil; T)
  $compStack:= [[x,m,e,$exitModeStack],:$compStack]
  nil

compNoStacking(x,m,e) ==
  T:= comp2(x,m,e) =>
    $useRepresentationHack and m=$EmptyMode and T.mode=$Representation => 
      [T.expr,"$",T.env]
    T
    --$Representation is bound in compDefineFunctor, set by doIt
    --this hack says that when something is undeclared, $ is
    --preferred to the underlying representation -- RDJ 9/12/83
    --Now that `per' and `rep' are built in, we use the above
    --hack only when `Rep' is defined the old way. -- gdr 2008/01/26
  compNoStacking1(x,m,e,$compStack)

compNoStacking1(x,m,e,$compStack) ==
  u:= get(RepIfRepHack m,"value",e) =>
    (T:= comp2(x,u.expr,e) => [T.expr,m,T.env]; nil)
  nil

comp2(x,m,e) ==
  [y,m',e]:= comp3(x,m,e) or return nil
  --if null atom y and isDomainForm(y,e) then e := addDomain(x,e)
        --line commented out to prevent adding derived domain forms
  m^=m' and ($bootStrapMode or isDomainForm(m',e))=>[y,m',addDomain(m',e)]
        --isDomainForm test needed to prevent error while compiling Ring
        --$bootStrapMode-test necessary for compiling Ring in $bootStrapMode
  [y,m',e]

comp3(x,m,$e) ==
  --returns a Triple or %else nil to signalcan't do'
  $e:= addDomain(m,$e)
  e:= $e --for debugging purposes
  m is ["Mapping",:.] => compWithMappingMode(x,m,e)
  m is ["QUOTE",a] => (x=a => [x,m,$e]; nil)
  STRINGP m => (atom x => (m=x or m=STRINGIMAGE x => [m,m,e]; nil); nil)
  -- In quasiquote mode, x should match exactly
  (y := isQuasiquote m) =>
     y = x => [["QUOTE",x], m, $e]
     nil
  atom x => compAtom(x,m,e)
  op:= first x
  getmode(op,e) is ["Mapping",:ml] and (u:= applyMapping(x,m,e,ml)) => u
  op=":" => compColon(x,m,e)
  op="::" => compCoerce(x,m,e)
  not ($insideCompTypeOf=true) and stringPrefix?('"TypeOf",PNAME op) =>
    compTypeOf(x,m,e)
  t:= compExpression(x,m,e)
  t is [x',m',e'] and not member(m',getDomainsInScope e') =>
    [x',m',addDomain(m',e')]
  t

compTypeOf(x:=[op,:argl],m,e) ==
  $insideCompTypeOf: local := true
  newModemap:= EQSUBSTLIST(argl,$FormalMapVariableList,get(op,'modemap,e))
  e:= put(op,'modemap,newModemap,e)
  comp3(x,m,e)

++ We just determined that `op' is called with argument list `args', where
++  `op' is either a local capsule function, or an external function 
++ with a local signature-import declaration.  Emit insn for the call.
emitLocalCallInsn: (%Symbol,%List,%Env) -> %Code
emitLocalCallInsn(op,args,e) ==
  op' :=          -- Find out the linkage name for `op'.
    get(op,"%Link",e) or encodeLocalFunctionName op
  get(op,"%Lang",e) => [op',:args]  -- non-Spad calling convention
  [op',:args,"$"]   

applyMapping([op,:argl],m,e,ml) ==
  #argl^=#ml-1 => nil
  isCategoryForm(first ml,e) =>
                                --is op a functor?
    pairlis:= [[v,:a] for a in argl for v in $FormalMapVariableList]
    ml' := SUBLIS(pairlis, ml)
    argl':=
      [T.expr for x in argl for m' in rest ml'] where
        T() == [.,.,e]:= comp(x,m',e) or return "failed"
    if argl'="failed" then return nil
    form:= [op,:argl']
    convert([form,first ml',e],m)
  argl':=
    [T.expr for x in argl for m' in rest ml] where
      T() == [.,.,e]:= comp(x,m',e) or return "failed"
  if argl'="failed" then return nil
  form:=
    atom op and not(op in $formalArgList) and null (u := get(op,"value",e)) =>
      emitLocalCallInsn(op,argl',e)
    -- Compiler synthetized operators are inline.
    u ^= nil and u.expr is ["XLAM",:.] => ["call",u.expr,:argl']
    ['call,['applyFun,op],:argl']
  pairlis:= [[v,:a] for a in argl' for v in $FormalMapVariableList]
  convert([form,SUBLIS(pairlis,first ml),e],m)

-- This version tends to give problems with #1 and categories
-- applyMapping([op,:argl],m,e,ml) ==
--   #argl^=#ml-1 => nil
--   mappingHasCategoryTarget :=
--     isCategoryForm(first ml,e) => --is op a functor?
--       form:= [op,:argl']
--       pairlis:= [[v,:a] for a in argl for v in $FormalMapVariableList]
--       ml:= SUBLIS(pairlis,ml)
--       true
--     false
--   argl':=
--     [T.expr for x in argl for m' in rest ml] where
--       T() == [.,.,e]:= comp(x,m',e) or return "failed"
--   if argl'="failed" then return nil
--   mappingHasCategoryTarget => convert([form,first ml,e],m)
--   form:=
--     not MEMQ(op,$formalArgList) and ATOM op =>
--       [op',:argl',"$"] where
--         op':= INTERN STRCONC(STRINGIMAGE $prefix,";",STRINGIMAGE op)
--     ["call",["applyFun",op],:argl']
--   pairlis:= [[v,:a] for a in argl' for v in $FormalMapVariableList]
--   convert([form,SUBLIS(pairlis,first ml),e],m)

hasFormalMapVariable(x, vl) ==
  $formalMapVariables: local := vl
  null vl => false
  ScanOrPairVec(function hasone?,x) where
     hasone? x == MEMQ(x,$formalMapVariables)

compWithMappingMode(x,m is ["Mapping",m',:sl],oldE) ==
  $killOptimizeIfTrue: local:= true
  e:= oldE
  isFunctor x =>
    if get(x,"modemap",$CategoryFrame) is [[[.,target,:argModeList],.],:.] and
      (and/[extendsCategoryForm("$",s,mode) for mode in argModeList for s in sl]
        ) and extendsCategoryForm("$",target,m') then return [x,m,e]
  if STRINGP x then x:= INTERN x
  for m in sl for v in (vl:= take(#sl,$FormalMapVariableList)) repeat
    [.,.,e]:= compMakeDeclaration(v,m,e)
  (vl ^= nil) and not hasFormalMapVariable(x, vl) => return
    [u,.,.] := comp([x,:vl],m',e) or return nil
    extractCodeAndConstructTriple(u, m, oldE)
  null vl and (t := comp([x], m', e)) => return
    [u,.,.] := t
    extractCodeAndConstructTriple(u, m, oldE)
  [u,.,.]:= comp(x,m',e) or return nil
  uu:=optimizeFunctionDef [nil,["LAMBDA",vl,u]]
  --  At this point, we have a function that we would like to pass.
  --  Unfortunately, it makes various free variable references outside
  --  itself.  So we build a mini-vector that contains them all, and
  --  pass this as the environment to our inner function.
  $FUNNAME :local := nil
  $FUNNAME__TAIL :local := [nil]
  expandedFunction:= transformToBackendCode second uu
  frees:=FreeList(expandedFunction,vl,nil,e)
    where FreeList(u,bound,free,e) ==
      atom u =>
        not IDENTP u => free
        MEMQ(u,bound) => free
        v:=ASSQ(u,free) =>
          RPLACD(v,1 + rest v)
          free
        null getmode(u,e) => free
        [[u,:1],:free]
      op := first u
      op in '(QUOTE GO function) => free
      op = "LAMBDA" =>
        bound := UNIONQ(bound, second u)
        for v in CDDR u repeat
          free:=FreeList(v,bound,free,e)
        free
      op = "PROG" =>
        bound := UNIONQ(bound, second u)
        for v in CDDR u | not atom v repeat
          free:=FreeList(v,bound,free,e)
        free
      op = "SEQ" =>
        for v in rest u | not atom v repeat
          free:=FreeList(v,bound,free,e)
        free
      op = "COND" =>
        for v in rest u repeat
          for vv in v repeat
            free:=FreeList(vv,bound,free,e)
        free
      if atom op then u := rest u  --Atomic functions aren't descended
      for v in u repeat
        free:=FreeList(v,bound,free,e)
      free
  expandedFunction :=
            --One free can go by itself, more than one needs a vector
         --An A-list name . number of times used
    #frees = 0 => ["LAMBDA",[:vl,"$$"], :CDDR expandedFunction]
    #frees = 1 =>
      vec:=first first frees
      ["LAMBDA",[:vl,vec], :CDDR expandedFunction]
    scode:=nil
    vec:=nil
    slist:=nil
    locals:=nil
    i:=-1
    for v in frees repeat
      i:=i+1
      vec:=[first v,:vec]
      rest v = 1 =>
                --Only used once
        slist:=[[first v,"getShellEntry","$$",i],:slist]
      scode:=[['SETQ,first v,["getShellEntry","$$",i]],:scode]
      locals:=[first v,:locals]
    body:=
      slist => SUBLISNQ(slist,CDDR expandedFunction)
      CDDR expandedFunction
    if locals then
      if body is [["DECLARE",:.],:.] then
        body := [first body,["PROG",locals,:scode,
                               ["RETURN",["PROGN",:rest body]]]]
      else body:=[["PROG",locals,:scode,["RETURN",["PROGN",:body]]]]
    vec:=["VECTOR",:nreverse vec]
    ["LAMBDA",[:vl,"$$"],:body]
  fname:=["CLOSEDFN",expandedFunction] --Like QUOTE, but gets compiled
  uu:=
    frees => ["CONS",fname,vec]
    ["LIST",fname]
  [uu,m,oldE]

extractCodeAndConstructTriple(u, m, oldE) ==
  u is ["call",fn,:.] =>
    if fn is ["applyFun",a] then fn := a
    [fn,m,oldE]
  [op,:.,env] := u
  [["CONS",["function",op],env],m,oldE]

compExpression(x,m,e) ==
  $insideExpressionIfTrue: local:= true
  -- special forms have dedicated compilers.
  (op := first x) and IDENTP op and (fn := GET(op,"SPECIAL")) =>
    FUNCALL(fn,x,m,e)
  compForm(x,m,e)

compAtomWithModemap(x,m,e,v) ==
  Tl :=
    [[transImplementation(x,map,fn),target,e]
      for map in v | map is [[.,target],[.,fn]]] =>
                                         --accept only monadic operators
        T:= or/[t for (t:= [.,target,.]) in Tl | modeEqual(m,target)] => T
        1=#(Tl:= [y for t in Tl | (y:= convert(t,m))]) => first Tl
        nil

transImplementation(op,map,fn) ==
  fn := genDeltaEntry [op,:map]
  fn is ["XLAM",:.] => [fn]
  ["call",fn]

compAtom(x,m,e) ==
  T:= compAtomWithModemap(x,m,e,get(x,"modemap",e)) => T
  x="nil" =>
    T:=
      modeIsAggregateOf('List,m,e) is [.,R] => compList(x,['List,R],e)
      return nil
    convert(T,m)
  t:=
    isSymbol x => compSymbol(x,m,e) or return nil
    member(m,$IOFormDomains) and primitiveType x => [x,m,e]
    STRINGP x => [x,x,e]
    [x,primitiveType x or return nil,e]
  convert(t,m)

primitiveType x ==
  x is nil => $EmptyMode
  STRINGP x => $String
  INTEGERP x =>
    x=0 => $NonNegativeInteger
    x>0 => $PositiveInteger
    $Integer
  FLOATP x => $DoubleFloat
  nil

compSymbol(s,m,e) ==
  s="$NoValue" => ["$NoValue",$NoValueMode,e]
  isFluid s => [s,getmode(s,e) or return nil,e]
  s=m or isLiteral(s,e) => [["QUOTE",s],s,e]
  v := get(s,"value",e) =>
    MEMQ(s,$functorLocalParameters) =>
        NRTgetLocalIndex s
        [s,v.mode,e] --s will be replaced by an ELT form in beforeCompile

    [s,v.mode,e] --s has been SETQd
  m':= getmode(s,e) =>
    if not MEMQ(s,$formalArgList) and not MEMQ(s,$FormalMapVariableList) and
      not isFunction(s,e) and null ($compForModeIfTrue=true) then errorRef s
    [s,m',e] --s is a declared argument
  MEMQ(s,$FormalMapVariableList) => 
    stackMessage('"no mode found for %1b",[s])
  member(m,$IOFormDomains) or member(m,[$Identifier,$Symbol]) =>
    [['QUOTE,s],m,e]
  not isFunction(s,e) => errorRef s

++ Return true if `m' is the most recent unique type case assumption 
++ on `x' that predates its declaration in environment `e'.
hasUniqueCaseView(x,m,e) ==
  props := getProplist(x,e)
  for [p,:v] in props repeat
    p = "condition" and v is [["case",.,t],:.] => return modeEqual(t,m)
    p = "value" => return false


convertOrCroak(T,m) ==
  u:= convert(T,m) => u
  userError ['"CANNOT CONVERT: ",T.expr,"%l",'" OF MODE: ",T.mode,"%l",
    '" TO MODE: ",m,"%l"]

convert(T,m) ==
  coerce(T,resolve(T.mode,m) or return nil)

mkUnion(a,b) ==
  b="$" and $Rep is ["Union",:l] => b
  a is ["Union",:l] =>
    b is ["Union",:l'] => ["Union",:union(l,l')]
    ["Union",:union([b],l)]
  b is ["Union",:l] => ["Union",:union([a],l)]
  ["Union",a,b]

hasType(x,e) ==
  fn get(x,"condition",e) where
    fn x ==
      null x => nil
      x is [["case",.,y],:.] => y
      fn rest x

--% General Forms

compForm(form,m,e) ==
  T:=
    compForm1(form,m,e) or compArgumentsAndTryAgain(form,m,e) or return
      stackMessageIfNone ["cannot compile","%b",form,"%d"]
  T

compArgumentsAndTryAgain(form is [.,:argl],m,e) ==
  -- used in case: f(g(x)) where f is in domain introduced by
  -- comping g, e.g. for (ELT (ELT x a) b), environment can have no
  -- modemap with selector b
  form is ["elt",a,.] =>
    ([.,.,e]:= comp(a,$EmptyMode,e) or return nil; compForm1(form,m,e))
  u:= for x in argl repeat [.,.,e]:= comp(x,$EmptyMode,e) or return "failed"
  u="failed" => nil
  compForm1(form,m,e)

outputComp(x,e) ==
  u:=comp(['_:_:,x,$OutputForm],$OutputForm,e) => u
  x is ['construct,:argl] =>
    [['LIST,:[([.,.,e]:=outputComp(x,e)).expr for x in argl]],$OutputForm,e]
  (v:= get(x,"value",e)) and (v.mode is ['Union,:l]) =>
    [['coerceUn2E,x,v.mode],$OutputForm,e]
  [x,$OutputForm,e]

compForm1(form is [op,:argl],m,e) ==
  op in $coreDiagnosticFunctions =>
    [[op,:[([.,.,e]:=outputComp(x,e)).expr for x in argl]],m,e]
  op is ["elt",domain,op'] =>
    domain="Lisp" =>
      --op'='QUOTE and null rest argl => [first argl,m,e]
      [[op',:[([.,.,e]:= compOrCroak(x,$EmptyMode,e)).expr for x in argl]],m,e]
    domain=$Expression and op'="construct" => compExpressionList(argl,m,e)
    domain is ["Foreign",lang] => compForeignPackageCall(lang,op',argl,m,e)
    (op'="COLLECT") and coerceable(domain,m,e) =>
      (T:= comp([op',:argl],domain,e) or return nil; coerce(T,m))
    -- Next clause added JHD 8/Feb/94: the clause after doesn't work
    -- since addDomain refuses to add modemaps from Mapping
    (domain is ['Mapping,:.]) and
      (ans := compForm2([op',:argl],m,e:= augModemapsFromDomain1(domain,domain,e),
        [x for x in getFormModemaps([op',:argl],e) | x is [[ =domain,:.],:.]]))             => ans
    ans := compForm2([op',:argl],m,e:= addDomain(domain,e),
      [x for x in getFormModemaps([op',:argl],e) | x is [[ =domain,:.],:.]])             => ans
    (op'="construct") and coerceable(domain,m,e) =>
      (T:= comp([op',:argl],domain,e) or return nil; coerce(T,m))
    nil

  (mmList:= getFormModemaps(form,e)) and (T:= compForm2(form,m,e,mmList)) => T
  compToApply(op,argl,m,e)

compExpressionList(argl,m,e) ==
  Tl:= [[.,.,e]:= comp(x,$Expression,e) or return "failed" for x in argl]
  Tl="failed" => nil
  convert([["LIST",:[y.expr for y in Tl]],$Expression,e],m)

compForm2(form is [op,:argl],m,e,modemapList) ==
  sargl:= TAKE(# argl, $TriangleVariableList)
  aList:= [[sa,:a] for a in argl for sa in sargl]
  modemapList:= SUBLIS(aList,modemapList)
  deleteList:=[]
  newList := []
  -- now delete any modemaps that are subsumed by something else, 
  -- provided the conditions are right (i.e. subsumer true 
  -- whenever subsumee true)
  for u in modemapList repeat
    if u is [[dc,:.],[cond,["Subsumed",.,nsig]]] and
       (v:=assoc([dc,:nsig],modemapList)) and v is [.,[ncond,:.]] then
           deleteList:=[u,:deleteList]
           if not PredImplies(ncond,cond) then
             newList := [[CAR u,[cond,['ELT,dc,nil]]],:newList]
  if deleteList then 
    modemapList := [u for u in modemapList | not MEMQ(u,deleteList)]
  -- We can use MEMQ since deleteList was built out of members of modemapList
  -- its important that subsumed ops (newList) be considered last
  if newList then 
    modemapList := append(modemapList,newList)

  -- The calling convention vector is used to determine when it is
  -- appropriate to infer type by compiling the argument vs. just
  -- looking up the parameter type for flag arguments.
  cc := checkCallingConvention([sig for [[.,:sig],:.] in modemapList], #argl)
  Tl:=
    [[.,.,e]:= T for x in argl for i in 0..
       while (T := inferMode(x,cc.i > 0,e))] where
      inferMode(x,flag,e) ==
        flag => [x,quasiquote x,e]
        isSimple x and compUniquely(x,$EmptyMode,e)

  or/[x for x in Tl] =>
    partialModeList:= [(x => x.mode; nil) for x in Tl]
    compFormPartiallyBottomUp(form,m,e,modemapList,partialModeList) or
      compForm3(form,m,e,modemapList)
  compForm3(form,m,e,modemapList)

++ We are about to compile a call.  Returns true if each argument
++ partially matches (as could be determined by type inference) the
++ corresponding expected type in the callee's modemap.
compFormMatch(mm,partialModeList) == main where
  main() ==
    mm is [[.,.,:argModeList],:.] and match(argModeList,partialModeList) 
      or wantArgumentsAsTuple(partialModeList,argModeList)
  match(a,b) ==
    null b => true
    null first b => match(rest a,rest b)
    first a=first b and match(rest a,rest b)

compFormPartiallyBottomUp(form,m,e,modemapList,partialModeList) ==
  mmList:= [mm for mm in modemapList | compFormMatch(mm,partialModeList)] =>
    compForm3(form,m,e,mmList)

compForm3(form is [op,:argl],m,e,modemapList) ==
  T:=
    or/
      [compFormWithModemap(form,m,e,first (mml:= ml))
        for ml in tails modemapList]
  $compUniquelyIfTrue =>
    or/[compFormWithModemap(form,m,e,mm) for mm in rest mml] =>
      THROW("compUniquely",nil)
    T
  T


compFormWithModemap(form,m,e,modemap) ==
  [map:= [.,target,:sig],[pred,impl]]:= modemap
  [op,:argl] := form := reshapeArgumentList(form,sig)
  if isCategoryForm(target,e) and isFunctor op then
    [modemap,e]:= substituteIntoFunctorModemap(argl,modemap,e) or return nil
    [map:= [.,target,:.],:cexpr]:= modemap
  sv:=listOfSharpVars map
  if sv then
     -- SAY [ "compiling ", op, " in compFormWithModemap,
     -- mode= ",map," sharp vars=",sv]
    for x in argl for ss in $FormalMapVariableList repeat
      if ss in sv then
        [map:= [.,target,:.],:cexpr]:= modemap :=SUBST(x,ss,modemap)
        -- SAY ["new map is",map]
  not coerceable(target,m,e) => nil
  [f,Tl]:= compApplyModemap(form,modemap,e) or return nil

  --generate code; return
  T:=
    [x',target,e'] where
      x':=
        form':= [f,:[t.expr for t in Tl]]
        target=$Category or isCategoryForm(target,e) => form'
        -- try to deal with new-style Unions where we know the conditions
        op = "elt" and f is ['XLAM,:.] and IDENTP(z := first argl) and
          (c:=get(z,'condition,e)) and
            c is [["case",=z,c1]] and
              (c1 is [":",=(second argl),=m] or EQ(c1,second argl) ) =>
-- first is a full tag, as placed by getInverseEnvironment
-- second is what getSuccessEnvironment will place there
                ["CDR",z]
        ["call",:form']
      e':=
        Tl => (LAST Tl).env
        e
  convert(T,m)

++ Returns the list of candidate modemaps for a form.  A modemap
++ is candidate for a form if its signature has the same number
++ of paramter types as arguments supplied to the form.  A special
++ case is made for a modemap whose sole parameter type is a Tuple.
++ In that case, it matches any number of supplied arguments.
getFormModemaps(form is [op,:argl],e) ==
  op is ["elt",domain,op1] =>
    [x for x in getFormModemaps([op1,:argl],e) | x is [[ =domain,:.],:.]]
  not atom op => nil
  modemapList:= get(op,"modemap",e)
  -- Within default implementations, modemaps cannot mention the
  -- current domain. 
  if $insideCategoryPackageIfTrue then
    modemapList := [x for x in modemapList | x is [[dom,:.],:.] and dom ^= '$]
  if op="elt"
     then modemapList:= eltModemapFilter(LAST argl,modemapList,e) or return nil
     else
      if op="setelt" then modemapList:=
        seteltModemapFilter(CADR argl,modemapList,e) or return nil
  nargs:= #argl
  finalModemapList:= [mm for (mm:= [[.,.,:sig],:.]) in modemapList 
                       | enoughArguments(argl,sig)]
  modemapList and null finalModemapList =>
    stackMessage('"no modemap for %1b with %2 arguments", [op,nargs])
  finalModemapList

++ We are either compiling a function call, or trying to determine
++ whether we know something about a function being defined with
++ parameters are not declared in the definition.  `sigs' is the list of
++ candidate signatures for `nargs' arguments or parameters.  We need
++ to detemine whether any of the arguments are flags.  If any
++ operation takes a flag argument, then all other overloads must have
++ the same arity and must take flag argument in the same position.
++ Returns a vector of length `nargs' with positive entries indicating
++ flag arguments, and negative entries for normal argument passing.
checkCallingConvention(sigs,nargs) ==
  v := makeFilledSimpleArray("%Short",nargs,0)
  for sig in sigs repeat
    for t in rest sig 
      for i in 0.. repeat
         isQuasiquote t => 
           v.i < 0 => userError '"flag argument restriction violation"
           v.i := v.i + 1
         v.i > 0 => userError '"flag argument restriction violation"
         v.i := v.i - 1
  v


eltModemapFilter(name,mmList,e) ==
  isConstantId(name,e) =>
    l:= [mm for mm in mmList | mm is [[.,.,.,sel,:.],:.] and sel=name] => l
            --there are elts with extra parameters
    stackMessage('"selector variable: %1b is undeclared and unbound",[name])
    nil
  mmList

seteltModemapFilter(name,mmList,e) ==
  isConstantId(name,e) =>
    l:= [mm for (mm:= [[.,.,.,sel,:.],:.]) in mmList | sel=name] => l
            --there are setelts with extra parameters
    stackMessage('"selector variable: %1b is undeclared and unbound",[name])
    nil
  mmList

compApplication(op,argl,m,T) ==
  e := T.env
  T.mode is ['Mapping, retm, :argml] =>
    #argl ^= #argml => nil
    retm := resolve(m, retm)
    retm = $Category or isCategoryForm(retm,e) => nil  -- not handled
    argTl := [[.,.,e] := comp(x,m,e) or return "failed"
              for x in argl for m in argml]
    argTl = "failed" => nil
    form:=
      atom T.expr and 
        not (MEMQ(op,$formalArgList) or MEMQ(T.expr,$formalArgList)) and
          null get(T.expr,"value",e) =>
            emitLocalCallInsn(T.expr,[a.expr for a in argTl],e)
      ['call, ['applyFun, T.expr], :[a.expr for a in argTl]]
    coerce([form, retm, e],resolve(retm,m))
  op = 'elt => nil
  eltForm := ['elt, op, :argl]
  comp(eltForm, m, e)

compToApply(op,argl,m,e) ==
  T:= compNoStacking(op,$EmptyMode,e) or return nil
  T.expr is ["QUOTE", =T.mode] => nil
  compApplication(op,argl,m,T)

++ `form' is a call to a operation described by the signature `sig'.
++ Massage the call so that homogeneous variable length argument lists
++ are properly tuplified.
reshapeArgumentList(form,sig) ==
  [op,:args] := form
  wantArgumentsAsTuple(args,sig) => [op,["%Comma",:args]]
  form

substituteIntoFunctorModemap(argl,modemap is [[dc,:sig],:.],e) ==
  #dc^=#sig =>
    keyedSystemError("S2GE0016",['"substituteIntoFunctorModemap",
      '"Incompatible maps"])
  #argl=#rest sig =>
                        --here, we actually have a functor form
    sig:= EQSUBSTLIST(argl,rest dc,sig)
      --make new modemap, subst. actual for formal parametersinto modemap
    Tl:= [[.,.,e]:= compOrCroak(a,m,e) for a in argl for m in rest sig]
    substitutionList:= [[x,:T.expr] for x in rest dc for T in Tl]
    [SUBLIS(substitutionList,modemap),e]
  nil

--% SPECIAL EVALUATION FUNCTIONS

compConstructorCategory(x,m,e) == [x,resolve($Category,m),e]

compString(x,m,e) == [x,resolve($StringCategory,m),e]

--% SUBSET CATEGORY

compSubsetCategory: (%Form,%Mode,%Env) -> %Maybe %Triple
compSubsetCategory(["SubsetCategory",cat,R],m,e) ==
  --1. put "Subsets" property on R to allow directly coercion to subset;
  --   allow automatic coercion from subset to R but not vice versa
  e:= put(R,"Subsets",[[$lhsOfColon,"isFalse"]],e)
  --2. give the subset domain modemaps of cat plus 3 new functions
  comp(["Join",cat,C'],m,e) where
    C'() ==
      substitute($lhsOfColon,"$",C'') where
        C''() ==
          ["CATEGORY","domain",["SIGNATURE","coerce",[R,"$"]],["SIGNATURE",
            "lift",[R,"$"]],["SIGNATURE","reduce",["$",R]]]

--% CONS

compCons: (%Form,%Mode,%Env) -> %Maybe %Triple
compCons1: (%Form,%Mode,%Env) -> %Maybe %Triple

compCons(form,m,e) == compCons1(form,m,e) or compForm(form,m,e)

compCons1(["CONS",x,y],m,e) ==
  [x,mx,e]:= comp(x,$EmptyMode,e) or return nil
  null y => convert([["LIST",x],["List",mx],e],m)
  yt:= [y,my,e]:= comp(y,$EmptyMode,e) or return nil
  T:=
    my is ["List",m',:.] =>
      mr:= ["List",resolve(m',mx) or return nil]
      yt':= convert(yt,mr) or return nil
      [x,.,e]:= convert([x,mx,yt'.env],second mr) or return nil
      yt'.expr is ["LIST",:.] => [["LIST",x,:rest yt'.expr],mr,e]
      [["CONS",x,yt'.expr],mr,e]
    [["CONS",x,y],["Pair",mx,my],e]
  convert(T,m)

--% SETQ

compSetq: (%List,%Thing,%List) -> %List
compSetq1: (%Form,%Thing,%Mode,%List) -> %List

compSetq(["%LET",form,val],m,E) == 
  compSetq1(form,val,m,E)

compSetq1(form,val,m,E) ==
  IDENTP form => setqSingle(form,val,m,E)
  form is [":",x,y] =>
    [.,.,E']:= compMakeDeclaration(x,y,E)
    compSetq(["%LET",x,val],m,E')
  form is [op,:l] =>
    op="CONS"  => setqMultiple(uncons form,val,m,E)
    op="%Comma" => setqMultiple(l,val,m,E)
    setqSetelt(form,val,m,E)

compMakeDeclaration: (%Form,%Mode,%Env) -> %Maybe %Triple
compMakeDeclaration(x,m,e) ==
  $insideExpressionIfTrue: local := false
  compColon([":",x,m],$EmptyMode,e)

setqSetelt([v,:s],val,m,E) ==
  comp(["setelt",v,:s,val],m,E)

setqSingle(id,val,m,E) ==
  checkVariableName id
  $insideSetqSingleIfTrue: local:= true
    --used for comping domain forms within functions
  currentProplist:= getProplist(id,E)
  m'':=
    get(id,"mode",E) or getmode(id,E) or
       (if m=$NoValueMode then $EmptyMode else m)
-- m'':= LASSOC("mode",currentProplist) or $EmptyMode
       --for above line to work, line 3 of compNoStacking is required
  T:=
    eval or return nil where
      eval() ==
        T:= comp(val,m'',E) => T
        not get(id,"mode",E) and m'' ^= (maxm'':=maximalSuperType m'') and
           (T:=comp(val,maxm'',E)) => T
        (T:= comp(val,$EmptyMode,E)) and getmode(T.mode,E) =>
          assignError(val,T.mode,id,m'')
  T':= [x,m',e']:= convert(T,m) or return nil
  if $profileCompiler = true then
    null IDENTP id => nil
    key :=
      id in rest $form => "arguments"
      "locals"
    profileRecord(key,id,T.mode)
  newProplist := 
    consProplistOf(id,currentProplist,"value",removeEnv [val,:rest T])
  e':= 
    CONSP id => e'
    addBinding(id,newProplist,e')
  if isDomainForm(val,e') then
    if isDomainInScope(id,e') then
      stackWarning('"domain valued variable %1b has been reassigned within its scope",[id])
    e':= augModemapsFromDomain1(id,val,e')
      --all we do now is to allocate a slot number for lhs
      --e.g. the %LET form below will be changed by putInLocalDomainReferences
--+
  if k := NRTassocIndex(id) then 
    form := ["setShellEntry","$",k,x]
  else form:=
         $QuickLet => ["%LET",id,x]
         ["%LET",id,x,
            (isDomainForm(x,e') => ['ELT,id,0];CAR outputComp(id,e'))]
  [form,m',e']

assignError(val,m',form,m) ==
  val =>
    stackMessage('"CANNOT ASSIGN: %1b OF MODE: %2pb TO: %3b OF MODE: %4bp",
      [val,m',form,m])
  stackMessage('"CANNOT ASSIGN: %1b TO: %2b OF MODE: %3pb",[val,form,m])

setqMultiple(nameList,val,m,e) ==
  val is ["CONS",:.] and m=$NoValueMode =>
    setqMultipleExplicit(nameList,uncons val,m,e)
  val is ["%Comma",:l] and m=$NoValueMode => setqMultipleExplicit(nameList,l,m,e)
  1 --create a gensym, %add to local environment, compile and assign rhs
  g:= genVariable()
  e:= addBinding(g,nil,e)
  T:= [.,m1,.]:= compSetq1(g,val,$EmptyMode,e) or return nil
  e:= put(g,"mode",m1,e)
  [x,m',e]:= convert(T,m) or return nil
  1.1 --exit if result is a list
  m1 is ["List",D] =>
    for y in nameList repeat 
      e:= put(y,"value",[genSomeVariable(),D,$noEnv],e)
    convert([["PROGN",x,["%LET",nameList,g],g],m',e],m)
  2 --verify that the #nameList = number of parts of right-hand-side
  selectorModePairs:=
                                                --list of modes
    decompose(m1,#nameList,e) or return nil where
      decompose(t,length,e) ==
        t is ["Record",:l] => [[name,:mode] for [":",name,mode] in l]
        comp(t,$EmptyMode,e) is [.,["RecordCategory",:l],.] =>
          [[name,:mode] for [":",name,mode] in l]
        stackMessage('"no multiple assigns to mode: %1p",[t])
  #nameList^=#selectorModePairs =>
    stackMessage('"%1b must decompose into %2 components",[val,#nameList])
  3 --generate code; return
  assignList:=
    [([.,.,e]:= compSetq1(x,["elt",g,y],z,e) or return "failed").expr
      for x in nameList for [y,:z] in selectorModePairs]
  if assignList="failed" then NIL
  else [MKPROGN [x,:assignList,g],m',e]

setqMultipleExplicit(nameList,valList,m,e) ==
  #nameList^=#valList =>
    stackMessage('"Multiple assignment error; # of items in: %1b must = # in: %2",[nameList,valList])
  gensymList:= [genVariable() for name in nameList]
  assignList:=
             --should be fixed to declare genVar when possible
    [[.,.,e]:= compSetq1(g,val,$EmptyMode,e) or return "failed"
      for g in gensymList for val in valList]
  assignList="failed" => nil
  reAssignList:=
    [[.,.,e]:= compSetq1(name,g,$EmptyMode,e) or return "failed"
      for g in gensymList for name in nameList]
  reAssignList="failed" => nil
  [["PROGN",:[T.expr for T in assignList],:[T.expr for T in reAssignList]],
    $NoValueMode, (LAST reAssignList).env]

--% Quasiquotation

++ Compile a quotation `[| form |]'.  form is not type-checked, and
++ is returned as is.  Note:  when get to support splicing, we would
++ need to scan `form' to see whether there is any computation that
++ must be done.
++ ??? Another strategy would be to infer a more accurate domain
++ ??? based on the meta operator, e.g. (DEF ...) would be a
++ DefinitionAst, etc.  That however requires that we have a full
++ fledged AST algebra -- which we don't have yet in mainstream.
compileQuasiquote: (%List,%Thing,%List) -> %List
compileQuasiquote(["[||]",:form],m,e) ==
  null form => nil
  coerce([["QUOTE", :form],$Syntax,e], m)


--% WHERE
compWhere: (%Form,%Mode,%Env) -> %Maybe %Triple
compWhere([.,form,:exprList],m,eInit) ==
  $insideExpressionIfTrue: local:= false
  $insideWhereIfTrue: local:= true
  e:= eInit
  u:=
    for item in exprList repeat
      [.,.,e]:= comp(item,$EmptyMode,e) or return "failed"
  u="failed" => return nil
  $insideWhereIfTrue:= false
  [x,m,eAfter]:= comp(macroExpand(form,eBefore:= e),m,e) or return nil
  eFinal:=
    del:= deltaContour(eAfter,eBefore) => addContour(del,eInit)
    eInit
  [x,m,eFinal]

compConstruct: (%Form,%Mode,%Env) -> %Maybe %Triple
compConstruct(form is ["construct",:l],m,e) ==
  y:= modeIsAggregateOf("List",m,e) =>
    T:= compList(l,["List",second y],e) => convert(T,m)
    compForm(form,m,e)
  y:= modeIsAggregateOf("Vector",m,e) =>
    T:= compVector(l,["Vector",second y],e) => convert(T,m)
    compForm(form,m,e)
  T:= compForm(form,m,e) => T
  for D in getDomainsInScope e repeat
    (y:=modeIsAggregateOf("List",D,e)) and
      (T:= compList(l,["List",second y],e)) and (T':= convert(T,m)) =>
         return T'
    (y:=modeIsAggregateOf("Vector",D,e)) and
      (T:= compVector(l,["Vector",second y],e)) and (T':= convert(T,m)) =>
         return T'

++ Compile a literal (quoted) symbol.
compQuote: (%Form,%Mode,%Env) -> %Maybe %Triple
compQuote(expr,m,e) == 
  expr is ["QUOTE",x] and IDENTP x => 
    -- Ideally, Identifier should be the default type.  However, for
    -- historical reasons we cannot afford that luxury yet.
    m = $Identifier or member(m,$IOFormDomains) => [expr,m,e]
    convert([expr,$Symbol,e],m)
  stackAndThrow('"%1b is not a literal symbol.",[x])

compList: (%Form,%Mode,%Env) -> %Maybe %Triple
compList(l,m is ["List",mUnder],e) ==
  null l => [NIL,m,e]
  Tl:= [[.,mUnder,e]:= comp(x,mUnder,e) or return "failed" for x in l]
  Tl="failed" => nil
  T:= [["LIST",:[T.expr for T in Tl]],["List",mUnder],e]

compVector: (%Form,%Mode,%Env) -> %Maybe %Triple
compVector(l,m is ["Vector",mUnder],e) ==
  Tl:= [[.,mUnder,e]:= comp(x,mUnder,e) or return "failed" for x in l]
  Tl="failed" => nil
  [["MAKE-ARRAY", #Tl, KEYWORD::ELEMENT_-TYPE, quoteForm getVMType mUnder,
     KEYWORD::INITIAL_-CONTENTS, ["LIST", :[T.expr for T in Tl]]],m,e]

--% MACROS

++ True if we are compiling a macro definition.
$macroIfTrue := false

compMacro(form,m,e) ==
  $macroIfTrue: local:= true
  ["MDEF",lhs,signature,specialCases,rhs]:= form
  if $verbose then
    prhs :=
      rhs is ['CATEGORY,:.] => ['"-- the constructor category"]
      rhs is ['Join,:.]     => ['"-- the constructor category"]
      rhs is ['CAPSULE,:.]  => ['"-- the constructor capsule"]
      rhs is ['add,:.]      => ['"-- the constructor capsule"]
      formatUnabbreviated rhs
    sayBrightly ['"   processing macro definition",'%b,
      :formatUnabbreviated lhs,'" ==> ",:prhs,'%d]
  m=$EmptyMode or m=$NoValueMode =>
    ["/throwAway",$NoValueMode,put(first lhs,"macro",macroExpand(rhs,e),e)]

--% SEQ

compSeq: (%Form,%Mode,%Env) -> %Maybe %Triple
compSeq1: (%Form,%List,%Env) -> %Maybe %Triple
compSeqItem: (%Thing,%Thing,%List) -> %List

compSeq(["SEQ",:l],m,e) == 
  compSeq1(l,[m,:$exitModeStack],e)

compSeq1(l,$exitModeStack,e) ==
  $insideExpressionIfTrue: local
  $finalEnv: local := nil   --used in replaceExitEtc.
  c:=
    [([.,.,e]:=
      --this used to be compOrCroak-- but changed so we can back out
        ($insideExpressionIfTrue:= NIL; compSeqItem(x,$NoValueMode,e) or return
          "failed")).expr for x in l]
  if c="failed" then return nil
  catchTag:= MKQ GENSYM()
  form:= ["SEQ",:replaceExitEtc(c,catchTag,"TAGGEDexit",$exitModeStack.(0))]
  [["CATCH",catchTag,form],$exitModeStack.(0),$finalEnv]

compSeqItem(x,m,e) == 
  comp(macroExpand(x,e),m,e)

replaceExitEtc(x,tag,opFlag,opMode) ==
  (fn(x,tag,opFlag,opMode); x) where
    fn(x,tag,opFlag,opMode) ==
      atom x => nil
      x is ["QUOTE",:.] => nil
      x is [ =opFlag,n,t] =>
        rplac(CAADDR x,replaceExitEtc(CAADDR x,tag,opFlag,opMode))
        n=0 =>
          $finalEnv:=
                  --bound in compSeq1 and compDefineCapsuleFunction
            $finalEnv => intersectionEnvironment($finalEnv,t.env)
            t.env
          rplac(first x,"THROW")
          rplac(CADR x,tag)
          rplac(CADDR x,(convertOrCroak(t,opMode)).expr)
        true => rplac(CADR x,CADR x-1)
      x is [key,n,t] and MEMQ(key,'(TAGGEDreturn TAGGEDexit)) =>
        rplac(first t,replaceExitEtc(first t,tag,opFlag,opMode))
      replaceExitEtc(first x,tag,opFlag,opMode)
      replaceExitEtc(rest x,tag,opFlag,opMode)

--% SUCHTHAT
compSuchthat: (%Form,%Mode,%Env) -> %Maybe %Triple
compSuchthat([.,x,p],m,e) ==
  [x',m',e]:= comp(x,m,e) or return nil
  [p',.,e]:= comp(p,$Boolean,e) or return nil
  e:= put(x',"condition",p',e)
  [x',m',e]

--% exit

compExit: (%Form,%Mode,%Env) -> %Maybe %Triple
compExit(["exit",level,x],m,e) ==
  index:= level-1
  $exitModeStack = [] => comp(x,m,e)
  m1:= $exitModeStack.index
  [x',m',e']:=
    u:=
      comp(x,m1,e) or return
        stackMessageIfNone ["cannot compile exit expression",x,"in mode",m1]
  modifyModeStack(m',index)
  [["TAGGEDexit",index,u],m,e]

modifyModeStack(m,index) ==
  $reportExitModeStack =>
    SAY("exitModeStack: ",COPY $exitModeStack," ====> ",
      ($exitModeStack.index:= resolve(m,$exitModeStack.index); $exitModeStack))
  $exitModeStack.index:= resolve(m,$exitModeStack.index)

compLeave: (%Form,%Mode,%Env) -> %Maybe %Triple
compLeave(["leave",level,x],m,e) ==
  index:= #$exitModeStack-1-$leaveLevelStack.(level-1)
  [x',m',e']:= u:= comp(x,$exitModeStack.index,e) or return nil
  modifyModeStack(m',index)
  [["TAGGEDexit",index,u],m,e]

--% return

compReturn: (%Form,%Mode,%Env) -> %Maybe %Triple
compReturn(["return",x],m,e) ==
  null $exitModeStack =>
    stackAndThrow('"the return before %1b is unneccessary",[x])
    nil
  index:= MAX(0,#$exitModeStack-1)
  if index >= 0 then 
    $returnMode:= resolve($exitModeStack.index,$returnMode)
  [x',m',e']:= u:= comp(x,$returnMode,e) or return nil
  if index>=0 then
    $returnMode:= resolve(m',$returnMode)
    modifyModeStack(m',index)
  [["TAGGEDreturn",0,u],m,e']

--% ELT

++ `op' supposedly designate an external entity with language linkage
++ `lang'.  Return the mode of its local declaration (import).
getExternalSymbolMode(op,lang,e) ==
  lang = "Builtin" => "%Thing"      -- for the time being
  lang ^= "C" =>
    stackAndThrow('"Sorry: %b Foreign %1b %d is invalid at the moment",[lang])
  get(op,"%Lang",e) ^= lang =>
    stackAndThrow('"%1bp is not known to have language linkage %2bp",[op,lang])
  getmode(op,e) or stackAndThrow('"Operator %1bp is not in scope",[op])

compElt: (%Form,%Mode,%Env) -> %Maybe %Triple
compElt(form,m,E) ==
  form isnt ["elt",aDomain,anOp] => compForm(form,m,E)
  aDomain="Lisp" or (aDomain is ["Foreign",lang] and lang="Builtin") =>
    [anOp',m,E] where anOp'() == (anOp = $Zero => 0; anOp = $One => 1; anOp)
  lang ^= nil =>
    opMode := getExternalSymbolMode(anOp,lang,E)
    op := get(anOp,"%Link",E) or anOp
    convert([op,opMode,E],m)
  isDomainForm(aDomain,E) =>
    E:= addDomain(aDomain,E)
    mmList:= getModemapListFromDomain(anOp,0,aDomain,E)
    modemap:=
      -- FIXME: do this only for constants.
      n:=#mmList
      1=n => mmList.(0)
      0=n =>
        return
          stackMessage('"Operation %1b missing from domain: %2p",
            [anOp,aDomain])
      stackWarning('"more than 1 modemap for: %1 with dc = %2p ===> %3",
        [anOp,aDomain,mmList])
      mmList.(0)
    [sig,[pred,val]]:= modemap
    #sig^=2 and ^val is ["elt",:.] => nil --what does the second clause do ????
    val := genDeltaEntry [opOf anOp,:modemap]
    convert([["call",val],first rest sig,E], m) --implies fn calls used to access constants
  compForm(form,m,E)

--% HAS

compHas: (%Form,%Mode,%Env) -> %Maybe %Triple
compHas(pred is ["has",a,b],m,$e) ==
  $e:= chaseInferences(pred,$e)
  predCode:= compHasFormat pred
  coerce([predCode,$Boolean,$e],m)

      --used in various other places to make the discrimination

compHasFormat (pred is ["has",olda,b]) ==
  argl := rest $form
  formals := TAKE(#argl,$FormalMapVariableList)
  a := SUBLISLIS(argl,formals,olda)
  [a,:.] := comp(a,$EmptyMode,$e) or return nil
  a := SUBLISLIS(formals,argl,a)
  b is ["ATTRIBUTE",c] => ["HasAttribute",a,["QUOTE",c]]
  b is ["SIGNATURE",op,sig,:.] =>
     ["HasSignature",a,
       mkList [MKQ op,mkList [mkTypeForm type for type in sig]]]
  isCategoryForm(b,$e) => ["HasCategory",a,mkTypeForm b]
  stackAndThrow('"Second argument to %1b must be a category, or a signature or an attribute",["has"])

--% IF

compIf: (%Form,%Mode,%Env) -> %Maybe %Triple
compPredicate: (%Form,%Env) -> %List
compFromIf: (%Form,%Mode,%Env) -> %Maybe %Triple

compIf(["IF",a,b,c],m,E) ==
  [xa,ma,Ea,Einv]:= compPredicate(a,E) or return nil
  [xb,mb,Eb]:= Tb:= compFromIf(b,m,Ea) or return nil
  [xc,mc,Ec]:= Tc:= compFromIf(c,resolve(mb,m),Einv) or return nil
  xb':= coerce(Tb,mc) or return nil
  x:= ["IF",xa,xb'.expr,xc]
  (returnEnv:= Env(xb'.env,Ec,xb'.expr,xc,E)) where
    Env(bEnv,cEnv,b,c,E) ==
      canReturn(b,0,0,true) =>
        (canReturn(c,0,0,true) => intersectionEnvironment(bEnv,cEnv); bEnv)
      canReturn(c,0,0,true) => cEnv
      E
  [x,mc,returnEnv]

canReturn(expr,level,exitCount,ValueFlag) ==  --SPAD: exit and friends
  atom expr => ValueFlag and level=exitCount
  (op:= first expr)="QUOTE" => ValueFlag and level=exitCount
  op="TAGGEDexit" =>
    expr is [.,count,data] => canReturn(data.expr,level,count,count=level)
  level=exitCount and not ValueFlag => nil
  op="SEQ" => or/[canReturn(u,level+1,exitCount,false) for u in rest expr]
  op="TAGGEDreturn" => nil
  op="CATCH" =>
    [.,gs,data]:= expr
    (findThrow(gs,data,level,exitCount,ValueFlag) => true) where
      findThrow(gs,expr,level,exitCount,ValueFlag) ==
        atom expr => nil
        expr is ["THROW", =gs,data] => true
            --this is pessimistic, but I know of no more accurate idea
        expr is ["SEQ",:l] =>
          or/[findThrow(gs,u,level+1,exitCount,ValueFlag) for u in l]
        or/[findThrow(gs,u,level,exitCount,ValueFlag) for u in rest expr]
    canReturn(data,level,exitCount,ValueFlag)
  op = "COND" =>
    level = exitCount =>
      or/[canReturn(last u,level,exitCount,ValueFlag) for u in rest expr]
    or/[or/[canReturn(u,level,exitCount,ValueFlag) for u in v]
                for v in rest expr]
  op="IF" =>
    expr is [.,a,b,c]
    if not canReturn(a,0,0,true) then
      SAY "IF statement can not cause consequents to be executed"
      pp expr
    canReturn(a,level,exitCount,nil) or canReturn(b,level,exitCount,ValueFlag)
      or canReturn(c,level,exitCount,ValueFlag)
  --now we have an ordinary form
  atom op => and/[canReturn(u,level,exitCount,ValueFlag) for u in expr]
  op is ["XLAM",args,bods] =>
    and/[canReturn(u,level,exitCount,ValueFlag) for u in expr]
  systemErrorHere ['"canReturn",expr] --for the time being

++ We are compiling a conditional expression, type check and generate
++ code for the predicate of the branch as a Boolean expression.
compPredicate(p,E) ==
  -- Ideally, we should be first inferring the type of the predicate
  -- `p'.  That would have the virtue of pointing out possible 
  -- ambiguities.  Then, on a second phase, implicitly coerce the
  -- the result to Boolean.  However, that would not quite work.  The 
  -- being that there are cases, such as equality, that are highgly
  -- ambiguous (e.g. see the various overloading of `=') for which it
  -- would be unfortunate to require more type annotation.  Note that
  -- the problem here is many misguided overloading of some operators.
  -- Consequently, we compile directly with Boolean as target.
  [p',m,E] := comp(p,$Boolean,E) or return nil
  [p',m,getSuccessEnvironment(p,E),getInverseEnvironment(p,E)]

getSuccessEnvironment(a,e) ==
  a is ["is",id,m] =>
    IDENTP id and isDomainForm(m,$EmptyEnvironment) =>
      e:=put(id,"specialCase",m,e)
      currentProplist:= getProplist(id,e)
      [.,.,e] := T := comp(m,$EmptyMode,e) or return nil -- duplicates compIs
      newProplist:= consProplistOf(id,currentProplist,"value",[m,:rest removeEnv T])
      addBinding(id,newProplist,e)
    e
  a is ["case",x,m] and IDENTP x =>
    put(x,"condition",[a,:get(x,"condition",e)],e)
  a is ["and",:args] =>
    for form in args repeat
      e := getSuccessEnvironment(form,e)
    e
  a is ["not",a'] => getInverseEnvironment(a',e)
  e

getInverseEnvironment(a,e) ==
  a is ["case",x,m] and IDENTP x =>
    --the next two lines are necessary to get 3-branched Unions to work
    -- old-style unions, that is
    (get(x,"condition",e) is [["OR",:oldpred]]) and member(a,oldpred) =>
      put(x,"condition",LIST MKPF(delete(a,oldpred),"OR"),e)
    getUnionMode(x,e) is ["Union",:l] =>
      l':= delete(m,l)
      for u in l' repeat
	 if u is ['_:,=m,:.] then l':= delete(u,l')
      newpred:= MKPF([["case",x,m'] for m' in l'],"OR")
      put(x,"condition",[newpred,:get(x,"condition",e)],e)
    e
  a is ["not",a'] => getSuccessEnvironment(a',e)
  e

getUnionMode(x,e) ==
  m:=
    atom x => getmode(x,e)
    return nil
  isUnionMode(m,e)

isUnionMode(m,e) ==
  m is ["Union",:.] => m
  (m':= getmode(m,e)) is ["Mapping",["UnionCategory",:.]] => CADR m'
  v:= get(RepIfRepHack m,"value",e) =>
    (v.expr is ["Union",:.] => v.expr; nil)
  nil

compFromIf(a,m,E) ==
  a="%noBranch" => ["%noBranch",m,E]
  comp(a,m,E)

compImport: (%Form,%Mode,%Env) -> %Triple
compImport(["import",:doms],m,e) ==
  for dom in doms repeat e:=addDomain(dom,e)
  ["/throwAway",$NoValueMode,e]

--% Foreign Function Interface

bootDenotation: %Symbol -> %Symbol
bootDenotation s == 
  INTERN(SYMBOL_-NAME s,"BOOTTRAN")

++ Return the Boot denotation of a basic FFI type.
getBasicFFIType: %Mode -> %Symbol
getBasicFFIType t ==
  t = $Byte => bootDenotation "byte"
  t = $Int16 => bootDenotation "int16"
  t = $Int32 => bootDenotation "int32"
--  t = $Int64 => bootDenotation "int64"
  t = $SingleInteger => bootDenotation "int"
  t = $SingleFloat =>  bootDenotation "float"
  t = $DoubleFloat => bootDenotation "double"
  t = $String => bootDenotation "string"
  nil


++ List of admissible type modifiers in an FFI import declaration.
$FFITypeModifier == '(ReadOnly WriteOnly ReadWrite)

++ Return the Boot denotation of an FFI datatype.  This is either
++ a basic VM type, or a simple array of sized integer or floating
++ point type.
getFFIDatatype: %Mode -> %Form
getFFIDatatype t ==
  x := getBasicFFIType t => x
  t is [m,["PrimitiveArray",t']] and m in $FFITypeModifier and
    member(t',[$Byte,$Int16,$Int32,$SingleFloat,$DoubleFloat]) =>
      m' := 
        m = "ReadOnly" => bootDenotation "readonly"
        m = "WriteOnly" => bootDenotation "writeonly"
        bootDenotation "readwrite"
      [m',[bootDenotation "buffer",getBasicFFIType t']]
  nil

++ Return the Boot denotation of a type that is valid in a external entity
++ signature.  
getBootType: %Mode -> %Form
getBootType t ==
  x := getFFIDatatype t => x
  t is ["Mapping",ret,:args] =>
    ret' := 
      ret = $Void => bootDenotation "void"
      getBasicFFIType ret or return nil
    args' := [getFFIDatatype arg or return "failed" for arg in args]
    args' = "failed" => return nil
    [bootDenotation "Mapping",ret',args']
  nil

++ Verify that mode `t' is admissible in an external entity signature
++ specification, and return its Boot denotation.
checkExternalEntityType(t,e) ==
  atom t => 
    stackAndThrow('"Type variable not allowed in import of external entity",nil)
  t' := getBootType t => t'
  stackAndThrow('"Type %1bp is invalid in a foreign signature",[t])


++ An external entity named `id' is being imported under signature
++ `type' from a foreign language `lang'.  Check that the import
++ is valid, and if so return the linkage name of the entity.
checkExternalEntity(id,type,lang,e) ==
  checkVariableName id
  -- An external entity name shall be unique in scope.
  getmode(id,e) =>
    stackAndThrow('"%1b is already in scope",[id])
  -- In particular, an external entity name cannot be overloaded
  -- with exported operators.
  get(id,"modemap",e) =>
    stackAndThrow('"%1b already names exported operations in scope",[id])
  -- We don't type check builtin declarations at the moment.
  lang = "Builtin" => id
  -- Only functions are accepted at the moment.  And all mentioned
  --  types must be those that are supported by the FFI.
  type' := checkExternalEntityType(type,e) 
  type' isnt [=bootDenotation "Mapping",:.] =>
    stackAndThrow('"Signature for external entity must be a Mapping type",nil)
  id' := encodeLocalFunctionName id
  [def] := genImportDeclaration(id',[bootDenotation "Signature",id,type'])
  compileLispDefinition(id,def)
  id'


++ Remove possible modifiers in the FFI type expression `t'.
removeModifiers t ==
  for (ts := [x,:.]) in tails t repeat
     x is [m,t'] and m in $FFITypeModifier =>
       rplac(first ts,t')
  t

++ Compile external entity signature import.
compSignatureImport: (%Form,%Mode,%Env) -> %Maybe %Triple
compSignatureImport(["%SignatureImport",id,type,home],m,e) ==
  -- 1. Make sure we have the right syntax.
  home isnt ["Foreign",:args] =>
    stackAndThrow('"signature import from be from a %1bp domain",["Foreign"])
  args isnt [lang] =>
    stackAndThrow('"%1bp takes exactly one argument",["Foreign"])
  not IDENTP lang =>
    stackAndThrow('"Argument to %1bp must be an identifier",["Foreign"])
  not MEMQ(lang, '(Builtin C)) =>
    stackAndThrow('"Sorry: Only %1bp is valid at the moment",["Foreign C"])
  -- 2. Make sure this import is not subverting anything we know
  id' := checkExternalEntity(id,type,lang,e)
  -- 3. Make a local declaration for it.
  T := compMakeDeclaration(id,removeModifiers type,e) or return nil
  T.env := put(id,"%Lang",lang,T.env)
  T.env:= put(id,"%Link",id',T.env)
  convert(T,m)


++ Compile package call to an external function.  
++ `lang' is the language calling convention
++ `op' is the operator name
++ `args' is the list of arguments
++ `m' is the context mode.
++ `e' is the compilation environment in effect.
compForeignPackageCall(lang,op,args,m,e) ==
  lang = "Builtin" =>
    -- Note: We don't rename builtin functions.
    [[op,:[([.,.,e]:= compOrCroak(x,$EmptyMode,e)).expr
             for x in args]],m,e]
  getExternalSymbolMode(op,lang,e) is ["Mapping",:argModes]
    and (#argModes = #args + 1) => applyMapping([op,:args],m,e,argModes)
  stackAndThrow('"OpenAxiom could not determine the meaning of %1bp",[op])

--% Compilation of logical operators that may have a pre-defined
--% meaning, or may need special handling because or short-circuiting
--% etc.

++ Compile a logical negation form `(not ...)'.
compLogicalNot: (%Form,%Mode,%Env) -> %Maybe %Triple
compLogicalNot(x,m,e) ==
  x isnt ["not", y] => nil
  -- ??? For the time being compiler values cannot handle operations
  -- ??? selected through general modemaps, and their semantics
  -- ??? are quite hardwired with their syntax.
  -- ??? Eventually, we should not need to do this.
  yTarget :=
    $normalizeTree and resolve(m,$Boolean) = $Boolean => $Boolean
    $EmptyMode
  yT := comp(y,yTarget,e) or return nil
  yTarget = $Boolean => (rplac(first yT, ["NOT",yT.expr]); yT)
  compResolveCall("not",[yT],m,yT.env)


++ Compile an exclusive `xor' expression.
compExclusiveOr: (%Form,%Mode,%Env) -> %Maybe %Triple
compExclusiveOr(x,m,e) ==
  x isnt ["xor",a,b] => nil
  aT := comp(a,$EmptyMode,e) or return nil
  e := 
    aT.mode = $Boolean => getSuccessEnvironment(a,aT.env)
    aT.env
  bT := comp(b,$EmptyMode,e) or return nil
  compResolveCall("xor",[aT,bT],m,bT.env)

--% Case
compCase: (%Form,%Mode,%Env) -> %Maybe %Triple
compCase1: (%Form,%Mode,%Env) -> %Maybe %Triple

--Will the jerk who commented out these two functions please NOT do so
--again.  These functions ARE needed, and case can NOT be done by
--modemap alone.  The reason is that A case B requires to take A
--evaluated, but B unevaluated.  Therefore a special function is
--required.  You may have thought that you had tested this on "failed"
--etc., but "failed" evaluates to it's own mode.  Try it on x case $
--next time.
--                An angry JHD - August 15th., 1984

compCase(["case",x,m'],m,e) ==
  e:= addDomain(m',e)
  T:= compCase1(x,m',e) => coerce(T,m)
  nil

compCase1(x,m,e) ==
  [x',m',e']:= comp(x,$EmptyMode,e) or return nil
  u:=
    [modemap
      for (modemap := [map,cexpr]) in getModemapList("case",2,e') 
        | map is [.,=$Boolean,s,t] and modeEqual(maybeSpliceMode t,m) 
            and modeEqual(s,m')] or return nil
  fn:= (or/[mm for (mm := [.,[cond,selfn]]) in u | cond=true]) or return nil
  fn := genDeltaEntry ["case",:fn]
  -- user-defined `case' functions really are binary, as opposed to
  -- the compiler-synthetized versions for Union instances.  
  not isUnionMode(m',e') => [["call",fn,x',MKQ m],$Boolean,e']
  [["call",fn,x'],$Boolean,e']


++ For `case' operation implemented in library, the second operand
++ (target type) is taken unevaluated. The corresponding parameter 
++ type in the modemap was specified as quasiquotation.  We
++ want to look at the actual type when comparing with modeEqual.
maybeSpliceMode: %Mode -> %Mode
maybeSpliceMode m ==
  (m' := isQuasiquote m) => m'
  m

compColon: (%Form,%Mode,%Env) -> %Maybe %Triple
compColon([":",f,t],m,e) ==
  $insideExpressionIfTrue=true => compColonInside(f,m,e,t)
    --if inside an expression, ":" means to convert to m "on faith"
  $lhsOfColon: local:= f
  t:=
    atom t and (t':= assoc(t,getDomainsInScope e)) => t'
    isDomainForm(t,e) and not $insideCategoryIfTrue =>
      (if not member(t,getDomainsInScope e) then e:= addDomain(t,e); t)
    isDomainForm(t,e) or isCategoryForm(t,e) => t
    t is ["Mapping",m',:r] => t
    STRINGP t => t              -- literal flag types are OK
    unknownTypeError t
    t
  f is ["LISTOF",:l] =>
    (for x in l repeat T:= [.,.,e]:= compColon([":",x,t],m,e); T)
  e:=
    f is [op,:argl] =>
      --for MPOLY--replace parameters by formal arguments: RDJ 3/83
      newTarget:= EQSUBSTLIST(take(#argl,$FormalMapVariableList),
        [(x is [":",a,m] => a; x) for x in argl],t)
      signature:=
        ["Mapping",newTarget,:
          [(x is [":",a,m] => m;
              getmode(x,e) or systemErrorHere ['"compColon",x]) for x in argl]]
      put(op,"mode",signature,e)
    put(f,"mode",t,e)
  if not $bootStrapMode and $insideFunctorIfTrue and
    makeCategoryForm(t,e) is [catform,e] then
        e:= put(f,"value",[genSomeVariable(),t,$noEnv],e)
  ["/throwAway",getmode(f,e),e]

unknownTypeError name ==
  name:=
    name is [op,:.] => op
    name
  stackAndThrow('"%1b is not a known type",[name])

compPretend: (%Form,%Mode,%Env) -> %Maybe %Triple
compPretend(["pretend",x,t],m,e) ==
  e:= addDomain(t,e)
  T:= comp(x,t,e) or comp(x,$EmptyMode,e) or return nil
  t' := T.mode            -- save this, in case we need to make suggestions
  T:= [T.expr,t,T.env]
  T':= coerce(T,m) => 
    -- If the `pretend' wasn't necessary, we should advise user to use
    -- less crude way of selecting expressions of thr `right type'.
    if t' = t then 
      stackWarning('"pretend %1p -- should replace by @",[t])
    T'
  nil

compColonInside(x,m,e,m') ==
  e:= addDomain(m',e)
  T:= comp(x,$EmptyMode,e) or return nil
  if (m'':=T.mode)=m' then warningMessage:= [":",m'," -- should replace by @"]
  T:= [T.expr,m',T.env]
  T':= coerce(T,m) =>
    if m'' = m' then
       stackWarning('": %1p -- should replace by @",[m'])
    else
       stackWarning('" : %1p -- replace by pretend", [m'])
    T'

compIs: (%Form,%Mode,%Env) -> %Maybe %Triple
compIs(["is",a,b],m,e) ==
  [aval,am,e] := comp(a,$EmptyMode,e) or 
    stackAndThrow('"Cannot determine the type of the expression %1b",[a])
  not isCategoryForm(am,e) =>
    stackAndThrow('"Expression %1b does not designate a domain",[a])
  [bval,bm,e] := comp(b,$EmptyMode,e) or return nil
  T:= [["domainEqual",aval,bval],$Boolean,e]
  coerce(T,m)

--%  Functions for coercion by the compiler

--  The function coerce is used by the old compiler for coercions.
--  The function coerceInteractive is used by the interpreter.
--  One should always call the correct function, since the represent-
--  ation of basic objects may not be the same.

tryCourtesyCoercion: (%Triple, %Mode) -> %Maybe %Triple
tryCourtesyCoercion(T,m) ==
  $InteractiveMode =>
    keyedSystemError("S2GE0016",['"coerce",
      '"function coerce called from the interpreter."])
  if $useRepresentationHack then
    rplac(CADR T,MSUBST("$",$Rep,CADR T))
  T':= coerceEasy(T,m) => T'
  T':= coerceSubset(T,m) => T'
  T':= coerceHard(T,m) => T'
  nil

coerce(T,m) ==
  T' := tryCourtesyCoercion(T,m) => T'
  isSomeDomainVariable m => nil
  stackMessage('"Cannot coerce %1b of mode %2pb to mode %3pb",
    [T.expr,T.mode,m])


coerceEasy: (%Triple,%Mode) -> %Maybe %Triple
coerceEasy(T,m) ==
  m=$EmptyMode => T
  m=$NoValueMode or m=$Void => [T.expr,m,T.env]
  T.mode =m => T
  T.mode =$NoValueMode => T
  T.mode =$Exit =>
      [["PROGN", T.expr, ["userError", '"Did not really exit."]],
        m,T.env]
  T.mode=$EmptyMode or modeEqualSubst(T.mode,m,T.env) =>
    [T.expr,m,T.env]

++ Return true if the VM constant form `val' is known to satisfy
++ the predicate `pred'.  Note that this is a fairly conservatism
++ approximation in the sense that the retunred value maye be false
++ for some other reasons, such as the predicate not being closed
++ with respect to the parameter `#1'.
satisfies(val,pred) ==
  pred=false or pred=true => pred
  vars := findVMFreeVars pred
  vars ^= nil and vars isnt ["#1"] => false
  eval ["LET",[["#1",val]],pred]


++ If the domain designated by the domain forms `m' and `m'' have
++ a common super domain, return least such super domaon (ordered
++ in terms of sub-domain relationship).  Otherwise, return nil.
commonSuperType(m,m') ==
  lineage := [m']
  while (t := superType m') ^= nil repeat
    lineage := [t,:lineage]
    m' := t
  while m ^= nil repeat
    member(m,lineage) => return m
    m := superType m

++ Coerce value `x' of mode `m' to mode `m'', if m is a subset of
++ of m'.  A special case is made for cross-subdomain conversion
++ for integral literals.
coerceSubset: (%Triple,%Mode) -> %Maybe %Triple
coerceSubset([x,m,e],m') ==
  isSubset(m,m',e) => [x,m',e]
  INTEGERP x and (m'' := commonSuperType(m,m')) =>
    -- obviously this is temporary
    satisfies(x,isSubDomain(m',m'')) => [x,m',e]
    nil
  nil

coerceHard: (%Triple,%Mode) -> %Maybe %Triple
coerceHard(T,m) ==
  $e: local:= T.env
  m':= T.mode
  STRINGP m' and modeEqual(m,$String) => [T.expr,m,$e]
  modeEqual(m',m) or
    (get(m',"value",$e) is [m'',:.] or getmode(m',$e) is ["Mapping",m'']) and
      modeEqual(m'',m) or
        (get(m,"value",$e) is [m'',:.] or getmode(m,$e) is ["Mapping",m'']) and
          modeEqual(m'',m') => [T.expr,m,T.env]
  STRINGP T.expr and T.expr=m => [T.expr,m,$e]
  isCategoryForm(m,$e) =>
      $bootStrapMode = true => [T.expr,m,$e]
      extendsCategoryForm(T.expr,T.mode,m) => [T.expr,m,$e]
      coerceExtraHard(T,m)
  (m' = "$" and m = $functorForm) or (m' = $functorForm and m = "$") =>
    [T.expr,m,$e]
  coerceExtraHard(T,m)

coerceExtraHard: (%Triple,%Mode) -> %Maybe %Triple
coerceExtraHard(T is [x,m',e],m) ==
  T':= autoCoerceByModemap(T,m) => T'
  isUnionMode(m',e) is ["Union",:l] and (t:= hasType(x,e)) and
    member(t,l) and (T':= autoCoerceByModemap(T,t)) and
      (T'':= coerce(T',m)) => T''
  m' is ['Record,:.] and m = $Expression =>
      [['coerceRe2E,x,['ELT,COPY m',0]],m,e]
  belongsTo?(m',["UnionType"],e) and hasUniqueCaseView(x,m,e) =>
    autoCoerceByModemap(T,m)
  -- Domain instantiations are first class objects
  m = $Domain =>
    m' = $Category => nil
    isCategoryForm(m',e) => [x,m',e]
    nil
  nil

++ returns true if mode `m' is known to belong to category `cat' in
++ the environment `e'.  This function is different from its cousines
++ `ofCategory', or `has'.  The latter perform runtime checks.  Here,
++ we are interested in a static approximation.  So, use with care.
belongsTo?(m,cat,e) ==
  c := get(m,"mode",e)
  c isnt ["Join",:cats] => nil
  member(cat,cats)

coerceable(m,m',e) ==
  m=m' => m
  tryCourtesyCoercion(["$fromCoerceable$",m,e],m') => m'
  nil

coerceExit: (%Triple,%Mode) -> %Maybe %Triple
coerceExit([x,m,e],m') ==
  m':= resolve(m,m')
  x':= replaceExitEtc(x,catchTag:= MKQ GENSYM(),"TAGGEDexit",$exitMode)
  coerce([["CATCH",catchTag,x'],m,e],m')

compAtSign: (%Form,%Mode,%Env) -> %Maybe %Triple
compAtSign(["@",x,m'],m,e) ==
  e:= addDomain(m',e)
  T:= comp(x,m',e) or return nil
  coerce(T,m)

compCoerce: (%Form,%Mode,%Env) -> %Maybe %Triple
compCoerce1: (%Form,%Mode,%Env) -> %Maybe %Triple
coerceByModemap: (%Maybe %Triple,%Mode) -> %Maybe %Triple
autoCoerceByModemap: (%Maybe %Triple,%Mode) -> %Maybe %Triple

compCoerce(["::",x,m'],m,e) ==
  e:= addDomain(m',e)
  T:= compCoerce1(x,m',e) => coerce(T,m)
  getmode(m',e) is ["Mapping",["UnionCategory",:l]] =>
    T:= (or/[compCoerce1(x,m1,e) for m1 in l]) or return nil
    coerce([T.expr,m',T.env],m)

++ Subroutine of compCoerce1.  If `T' is a triple whose mode is
++ a super-domain of `sub', then return code that performs the
++ checked courtesy coercion to `sub'.
coerceSuperset: (%Triple, %Mode) -> %Maybe %Triple
coerceSuperset(T,sub) ==
  sub = "$" =>
    T' := coerceSuperset(T,$functorForm) or return nil
    rplac(second T',"$")
    T'
  pred := isSubset(sub,T.mode,T.env) =>
    -- Don't bother introducing a temporary if we have an
    -- atomic expression.
    simple? := atom T.expr and not MEMQ(T.expr,$functorLocalParameters)
    g := 
      simple? => T.expr
      GENSYM()
    result := 
      simple? => g
      ["%LET",g,T.expr]
    pred := substitute(g,"#1",pred)
    code := ["PROG1",result, ["check-subtype",pred,MKQ sub,g]]
    [code,sub,T.env]
  nil

compCoerce1(x,m',e) ==
  T:= comp(x,m',e) or comp(x,$EmptyMode,e) or return nil
  m1:=
    STRINGP T.mode => $String
    T.mode
  m':=resolve(m1,m')
  T:=[T.expr,m1,T.env]
  T':= coerce(T,m') => T'
  T':= coerceByModemap(T,m') => T'
  T' := coerceSuperset(T,m') => T'
  nil

coerceByModemap([x,m,e],m') ==
--+ modified 6/27 for new runtime system
  u:=
    [modemap
      for (modemap:= [map,cexpr]) in getModemapList("coerce",1,e) | map is [.,t,
        s] and (modeEqual(t,m') or isSubset(t,m',e))
           and (modeEqual(s,m) or isSubset(m,s,e))] or return nil

  --mm:= (or/[mm for (mm:=[.,[cond,.]]) in u | cond=true]) or return nil
  mm:=first u  -- patch for non-trival conditons
  fn :=
    genDeltaEntry ['coerce,:mm]
  [["call",fn,x],m',e]

autoCoerceByModemap([x,source,e],target) ==
  u:=
    [modemap
      for (modemap:= [map,cexpr]) in getModemapList("autoCoerce",1,e)
        | map is [.,t,s] and modeEqual(t,target) 
            and modeEqual(s,source)] or return nil
  fn:= (or/[mm for (mm := [.,[cond,selfn]]) in u | cond=true]) or return nil

  source is ["Union",:l] and member(target,l) =>
    (y:= get(x,"condition",e)) and (or/[u is ["case",., =target] for u in y])
       => [["call",genDeltaEntry ["autoCoerce", :fn],x],target,e]
    x="$fromCoerceable$" => nil
    stackMessage('"cannot coerce %1b of mode %2pb to %3pb without a case statement",
      [x,source,target])
  [["call",genDeltaEntry ["autoCoerce", :fn],x],target,e]


++ Compile a comma separated expression list. These typically are
++ tuple objects, or argument list in a call to a homogeneous
++ vararg operations.
compComma: (%Form,%Mode,%Env) -> %Maybe %Triple
compComma(form,m,e) ==
  form isnt ["%Comma",:argl] => systemErrorHere ["compComma",form]
  Tl := [comp(a,$EmptyMode,e) or return "failed" for a in argl]
  Tl = "failed" => nil
  -- ??? Ideally, we would like to compile to a Cross type, then
  -- convert to the target type.  However, the current compiler and
  -- runtime data structures are not regular enough in their interfaces;
  -- so we make a special rule when compiling with a Tuple as target,
  -- we do the convertion here (instead of calling convert).  Semantically,
  -- there should be no difference, but it makes the compiler code
  -- less regular, with duplicated effort.
  m is ["Tuple",t] =>
    Tl' := [convert(T,t) or return "failed" for T in Tl]
    Tl' = "failed" => nil
    [["asTupleNew0", ["getVMType",t], [T.expr for T in Tl']], m, e]
  T := [["LIST2VEC", [T.expr for T in Tl]], 
        ["Cross",:[T.mode for T in Tl]], e]
  convert(T,m)

--% Very old resolve
-- should only be used in the old (preWATT) compiler

resolve(din,dout) ==
  din=$NoValueMode or dout=$NoValueMode => $NoValueMode
  dout=$EmptyMode => din
  din^=dout and (STRINGP din or STRINGP dout) =>
    modeEqual(dout,$String) => dout
    modeEqual(din,$String) => nil
    mkUnion(din,dout)
  dout

modeEqual(x,y) ==
  -- this is the late modeEqual
  -- orders Unions
  atom x or atom y => x=y
  #x ^=#y => nil
  x is ['Union,:xl] and y is ['Union,:yl] =>
    for x1 in xl repeat
      for y1 in yl repeat
        modeEqual(x1,y1) =>
          xl := delete(x1,xl)
          yl := delete(y1,yl)
          return nil
    xl or yl => nil
    true
  (and/[modeEqual(u,v) for u in x for v in y])

modeEqualSubst(m1,m,e) ==
  modeEqual(m1, m) => true
  atom m1 => get(m1,"value",e) is [m',:.] and modeEqual(m',m)
  m1 is [op,:l1] and m is [=op,:l2]  and # l1 = # l2 =>
-- Above length test inserted JHD 4:47 on 15/8/86
-- Otherwise Records can get fouled up - consider expressIdealElt
-- in the DEFAULTS package
        and/[modeEqualSubst(xm1,xm2,e) for xm1 in l1 for xm2 in l2]
  nil

--% Categories

compCat(form is [functorName,:argl],m,e) ==
  fn:= GETL(functorName,"makeFunctionList") or return nil
  diagnoseUnknownType(form,e)
  [funList,e]:= FUNCALL(fn,form,form,e)
  catForm:=
    ["Join",'(SetCategory),["CATEGORY","domain",:
      [["SIGNATURE",op,sig] for [op,sig,.] in funList | op^="="]]]
  --RDJ: for coercion purposes, it necessary to know it's a Set; I'm not
  --sure if it uses any of the other signatures(see extendsCategoryForm)
  [form,catForm,e]
 
--% APPLY MODEMAPS

++ `op' has been selected as a viable candidate exported operation, 
++ for argument triple list `argTl', modemap `mm'.
++ Return the most refined implementation that makes the call successful.
compViableModemap(op,argTl,mm) ==
  [[dc,.,:margl],fnsel] := mm
  -- 1. Give up if the call is hopeless.
  argTl := [coerce(x,m) or return "failed" for x in argTl for m in margl]
  argTl = "failed" => nil

  -- 2.  obtain domain-specific function, if possible
  f := compMapCond(dc,fnsel) or return nil

  -- 3. Mark `f' as used.
  -- We can no longer trust what the modemap says for a reference into
  -- an exterior domain (it is calculating the displacement based on view
  -- information which is no longer valid; thus ignore this index and
  -- store the signature instead.
  f is [op1,.,.] and MEMQ(op1,'(ELT CONST Subsumed)) =>
    [genDeltaEntry [op,:mm],argTl]
  [f,argTl]

compApplyModemap(form,modemap,$e) ==
  [op,:argl] := form                   --form to be compiled
  [[mc,mr,:margl],fnsel] := modemap    --modemap we are testing

  -- $e     is the current environment

  -- 0.  fail immediately if #argl=#margl
  if #argl^=#margl then return nil

  -- 1.  use modemap to evaluate arguments, returning failed if
  --     not possible
  lt:=
    [[.,.,$e]:= comp(y,m,$e) or return "failed"
            for y in argl for m in margl]
  lt="failed" => return nil

  -- 2. Select viable modemap implementation.
  compViableModemap(op,lt,modemap)

compMapCond': (%Form,%Mode) -> %Boolean
compMapCond'(cexpr,dc) ==
  cexpr=true => true
  cexpr is ["AND",:l] => and/[compMapCond'(u,dc) for u in l]
  cexpr is ["OR",:l] => or/[compMapCond'(u,dc) for u in l]
  cexpr is ["not",u] => not compMapCond'(u,dc)
  cexpr is ["has",name,cat] => (knownInfo cexpr => true; false)
        --for the time being we'll stop here - shouldn't happen so far
        --$disregardConditionIfTrue => true
        --stackSemanticError(("not known that",'%b,name,
        -- '%d,"has",'%b,cat,'%d),nil)
  --now it must be an attribute
  member(["ATTRIBUTE",dc,cexpr],get("$Information","special",$e)) => true
  --for the time being we'll stop here - shouldn't happen so far
  stackMessage('"not known that %1pb has %2pb",[dc,cexpr])
  false

compMapCond: (%Mode,%List) -> %Code
compMapCond(dc,[cexpr,fnexpr]) ==
  compMapCond'(cexpr,dc) => fnexpr
  stackMessage('"not known that %1pb has %2pb",[dc,cexpr])


--%

compResolveCall(op,argTs,m,$e) ==
  outcomes := 
    [t for mm in getModemapList(op,#argTs,$e) | t := tryMM] where
       tryMM() ==
         not coerceable(mm.mmTarget,m,$e) =>nil
         compViableModemap(op,argTs,mm) isnt [f,Ts] => nil
         coerce([["call",f,:[T.expr for T in Ts]],mm.mmTarget,$e],m)
  #outcomes ^= 1 => nil
  first outcomes

--% %Match


++ Subroutine of compMatch, responsible of compiling individual alternative
++ of the form 
++      x@t => stmt
++ in environment `e'.  Here `y' is the scrutinee, and `m' is the
++ exit mode of `stmt'. And `T' is [y,m,e].
++ Return a quadruple [code,mode,envTrue,envFalse], where
++   code is a pair [cond, body]
++   mode is the final mode (equal to m if everything is OK)
++   envTrue is the environment resulting from compiling `stmt'
++   envFalse is the environment for failed match.
compRetractAlternative(x,t,stmt,m,s,T) ==
  -- The retract pattern is compiled by transforming
  --     x@t => sttmt
  -- into the following program fragment
  --   y case t => (x := <init>; stmt)
  -- where <init> is code that compute appropriate initialization
  -- for `x' under the condition that either `y' may be implicitly
  -- convertible to t (using only courtesy coerciions) or that
  -- `y' is retractable to t.
  --
  -- 1.  Evaluate the retract condition, and retract.
  y := T.expr            -- guaranteed to be a name.
  e := T.env
  caseCode := nil
  restrictCode := nil
  envFalse := e
  -- 1.1. Try courtesy coercions first.  That way we can use 
  --      optimized versions where available.  That also
  --      makes the scheme work for untagged unions.
  if testT := compPredicate(["case",y,t],e) then
    [caseCode,.,e,envFalse] := testT
    [restrictCode,.,e] := 
      tryCourtesyCoercion([y,T.mode,e],t) or
        comp(["retract",y],t,e) or return 
          stackAndThrow('"Could not retract %1 to type %2bp",[s,t])
  -- 1.2. Otherwise try retractIfCan, for those `% has RetractableTo t'.
  else if retractT := comp(["retractIfCan",y],["Union",t,'"failed"],e) then
    [retractCode,.,e] := retractT
    -- Assign this value to a temporary.  From the backend point of
    -- view, that temporary needs to have a lifetime that covers both
    -- the condition and the body of the alternative, so just use 
    -- assignment here and let the rest of the compiler deal with it.
    z := GENSYM()
    caseCode := ["PROGN",["%LET",z,retractCode],["QEQCAR",z,0]]
    restrictCode := ["QCDR",z]
  -- 1.3. Everything else failed; nice try.
  else return stackAndThrow('"%1 is not retractable to %2bp",[s,t])
  -- 2.  Now declare `x'.
  [.,.,e] := compMakeDeclaration(x,t,e) or return nil
  e := put(x,"value",[genSomeVariable(),t,$noEnv],e)
  -- 3.  Compile body of the retract pattern.
  stmtT := comp(stmt,m,e) or return 
    stackAndThrow('"could not compile %1b under mode %2pb",[stmt,m])
  -- 4.  Generate code for the whole pattern.
  code := [caseCode, ["LET",[[x,restrictCode]],stmtT.expr]]
  [code,stmtT.mode,stmtT.env,envFalse]


++ Subroutine of compMatch, responsible for compiling alternative of
++ of the form
++    x: t => stmt
++ in environment `e', where `y' is the scrutinee, and `m' is the
++ exit mode of `stmt'. And `T' is [y,m,e].
++ Return a quadruple [code,mode,envTrue,envFalse], where
++   code is a pair [cond, body]
++   mode is the final mode (equal to m if everything is OK)
++   env is the environment resulting from compiling `stmt'
compRecoverAlternative(x,t,stmt,m,s,T) ==
  -- The retract pattern is compiled by transforming
  --     x: t => sttmt
  -- into the following program fragment
  --   domainOf y is t => (x := <init>; stmt)
  -- where <init> is code that compute appropriate initialization
  -- for `x' under the condition that y if of type Any, and the
  -- underlying type is t.
  --
  -- 1.  Evaluate the recovery condition
  y := T.expr              -- guaranteed to be a name.
  e := T.env
  T.mode ^= $Any =>
    stackAndThrow('"Scrutinee must be of type %b Any %d in type recovery alternative of case pattern",nil)
  caseCode := ["EQUAL",["devaluate",t],["objMode",y]]
  -- 2. Declare `x'.
  [.,.,e] := compMakeDeclaration(x,t,e) or return nil
  e := put(x,"value",[genSomeVariable(),t,$noEnv],e)
  -- 3. Compile body of alternative
  stmtT := comp(stmt,m,e) or return 
    stackAndThrow('"could not compile %1b under mode %2pb",[stmt,m])
  -- 4.  Assemble code
  code := [caseCode,["LET",[[x,["objVal",y]]],stmtT.expr]]
  [code,stmtT.mode,stmtT.env,e]

warnUnreachableAlternative pat ==
  stackWarning('"Alternative with pattern %1b will not be reached",[pat])

warnTooManyOtherwise() ==
  stackWarning('"One too many `otherwise' alternative",nil)

compMatch(["%Match",subject,altBlock],m,e) ==
  altBlock isnt ["%Block",:alts] => 
    stackAndThrow('"case pattern must specify block of alternatives",nil)
  savedEnv := e
  -- 1.  subjectTmp := subject
  [se,sm,e] := comp(subject,$EmptyMode,e) or return nil
  sn := GENSYM()
  [.,.,e] := compMakeDeclaration(sn,sm,e) or return nil
  e := put(sn,"value",[genSomeVariable(),sm,$noEnv],e)
  -- 2. compile alternatives.
  altsCode := nil
  catchAllCount := 0
  for alt in alts repeat
     alt is ["=>",pat,stmt] =>
       pat is [op,x,t] and op in '(_: _@) =>
         not IDENTP x => 
           stackAndThrow('"pattern %1b must declare a variable",[pat])
         if catchAllCount > 0 then
           warnUnreachableAlternative pat
         [code,mode,.,e] :=
           op = ":" => compRecoverAlternative(x,t,stmt,m,subject,[sn,sm,e])
           compRetractAlternative(x,t,stmt,m,subject,[sn,sm,e])
             or return stackAndThrow('"cannot compile %1b",[alt])
         altsCode := [code,:altsCode]
       pat = "otherwise" =>
         if catchAllCount > 0 then
           warnTooManyOtherwise()
         catchAllCount := catchAllCount + 1
         [code,.,e] := comp(stmt,m,e) or return
                         stackAndThrow('"cannot compile",[stmt])
         altsCode := [[true,code],:altsCode]
       return stackAndThrow('"invalid pattern %1b",[pat])
     return stackAndThrow('"invalid alternative %1b",[alt])
  catchAllCount = 0 => 
    stackAndThrow('"missing %b otherwise %d alternative in case pattern",nil)
  code := ["LET",[[sn,se]],["COND",:nreverse altsCode]]
  [code,m,savedEnv]


--%
--% Inline Requests
--%

++ We are processing a capsule and `t is nominated in an inline
++ directive.  This means that the compiler can `bypass' the usual 
++ indirect call through domain interface and attempt to resolve
++ modemap references.  
++ Concretely, this means that `t is evaluated.
processInlineRequest(t,e) ==
  T := compOrCroak(t,$EmptyMode,e)
  not isCategoryForm(T.mode,e) =>
    stackAndThrow('"%1b does not designate a domain",[t])
  atom T.expr =>
    stackWarning('"inline request for type variable %1bp is meaningless",[t])
  T.expr is [ctor] =>
    $optimizableConstructorNames := [ctor,:$optimizableConstructorNames]
  -- Don't try too hard; the current domain evaluation is insane.
  stackWarning('"Ignoring inline arequest for non-niladic type %1bp",[t])


--%
--% ITERATORS
--%
 
compReduce(form,m,e) ==
 compReduce1(form,m,e,$formalArgList)

compReduce1(form is ["REDUCE",op,.,collectForm],m,e,$formalArgList) ==
  [collectOp,:itl,body]:= collectForm
  if STRINGP op then op:= INTERN op
  ^MEMQ(collectOp,'(COLLECT COLLECTV COLLECTVEC)) =>
        systemError ['"illegal reduction form:",form]
  $sideEffectsList: local := nil
  $until: local := nil
  $initList: local := nil
  $endTestList: local := nil
  oldEnv := e
  $e:= e
  itl:= [([.,$e]:= compIterator(x,$e) or return "failed").(0) for x in itl]
  itl="failed" => return nil
  e:= $e
  acc:= GENSYM()
  afterFirst:= GENSYM()
  bodyVal:= GENSYM()
  [part1,m,e]:= comp(["%LET",bodyVal,body],m,e) or return nil
  [part2,.,e]:= comp(["%LET",acc,bodyVal],m,e) or return nil
  [part3,.,e]:= comp(["%LET",acc,parseTran [op,acc,bodyVal]],m,e) or return nil
  identityCode:=
    id:= getIdentity(op,e) => u.expr where u() == comp(id,m,e) or return nil
    ["IdentityError",MKQ op]
  finalCode:=
    ["PROGN",
      ["%LET",afterFirst,nil],
       ["REPEAT",:itl,
        ["PROGN",part1,
          ["IF", afterFirst,part3,
                   ["PROGN",part2,["%LET",afterFirst,MKQ true]]]]],
                    ["IF",afterFirst,acc,identityCode]]
  if $until then
    [untilCode,.,e]:= comp($until,$Boolean,e)
    finalCode:= substitute(["UNTIL",untilCode],'$until,finalCode)
  [finalCode,m,oldEnv]

++ returns the identity element of the `reduction' operation `x'
++ over a list -- a monoid homomorphism.   
getIdentity(x,e) ==
  -- The empty list should be indicated by name, not  by its
  -- object representation.
  GETL(x,"THETA") is [y] => (y => y; "nil")
 
numberize x ==
  x=$Zero => 0
  x=$One => 1
  atom x => x
  [numberize first x,:numberize rest x]
 
compRepeatOrCollect(form,m,e) ==
  fn(form,[m,:$exitModeStack],[#$exitModeStack,:$leaveLevelStack],$formalArgList
    ,e) where
      fn(form,$exitModeStack,$leaveLevelStack,$formalArgList,e) ==
        $until: local := nil
        oldEnv := e
        [repeatOrCollect,:itl,body]:= form
        itl':=
          [([x',e]:= compIterator(x,e) or return "failed"; x') for x in itl]
        itl'="failed" => nil
        targetMode:= first $exitModeStack
        bodyMode:=
          repeatOrCollect="COLLECT" =>
            targetMode = '$EmptyMode => '$EmptyMode
            (u:=modeIsAggregateOf('List,targetMode,e)) =>
              CADR u
            (u:=modeIsAggregateOf('PrimitiveArray,targetMode,e)) =>
              repeatOrCollect:='COLLECTV
              CADR u
            (u:=modeIsAggregateOf('Vector,targetMode,e)) =>
              repeatOrCollect:='COLLECTVEC
              CADR u
            stackMessage('"Invalid collect bodytype")
            return nil
            -- If we're doing a collect, and the type isn't conformable
            -- then we've boobed. JHD 26.July.1990
          $NoValueMode
        [body',m',e']:=
            compOrCroak(body,bodyMode,e) or return nil
        if $until then
          [untilCode,.,e']:= comp($until,$Boolean,e')
          itl':= substitute(["UNTIL",untilCode],'$until,itl')
        form':= [repeatOrCollect,:itl',body']
        m'':=
          repeatOrCollect="COLLECT" =>
            (u:=modeIsAggregateOf('List,targetMode,e)) => CAR u
            ["List",m']
          repeatOrCollect="COLLECTV" =>
            (u:=modeIsAggregateOf('PrimitiveArray,targetMode,e)) => CAR u
            ["PrimitiveArray",m']
          repeatOrCollect="COLLECTVEC" =>
            (u:=modeIsAggregateOf('Vector,targetMode,e)) => CAR u
            ["Vector",m']
          m'
        T := coerceExit([form',m'',e'],targetMode) or return nil
        -- iterator variables and other variables declared in
        -- in a loop are local to the loop.
        [T.expr,T.mode,oldEnv]
 
--constructByModemap([x,source,e],target) ==
--  u:=
--    [cexpr
--      for (modemap:= [map,cexpr]) in getModemapList("construct",1,e) | map is [
--        .,t,s] and modeEqual(t,target) and modeEqual(s,source)] or return nil
--  fn:= (or/[selfn for [cond,selfn] in u | cond=true]) or return nil
--  [["call",fn,x],target,e]
 
listOrVectorElementMode x ==
  x is [a,b,:.] and member(a,'(PrimitiveArray Vector List)) => b
 
compIterator(it,e) ==
    -- ??? Allow for declared iterator variable.
  it is ["IN",x,y] =>
    checkVariableName x
    --these two lines must be in this order, to get "for f in list f"
    --to give  an error message if f is undefined
    [y',m,e]:= comp(y,$EmptyMode,e) or return nil
    $formalArgList:= [x,:$formalArgList]
    [mOver,mUnder]:=
      modeIsAggregateOf("List",m,e) or return
         stackMessage('"mode: %1pb must be a list of some mode",[m])
    if null get(x,"mode",e) then [.,.,e]:=
      compMakeDeclaration(x,mUnder,e) or return nil
    e:= put(x,"value",[genSomeVariable(),mUnder,$noEnv],e)
    [y'',m'',e] := coerce([y',m,e], mOver) or return nil
    [["IN",x,y''],e]
  it is ["ON",x,y] =>
    checkVariableName x
    $formalArgList:= [x,:$formalArgList]
    [y',m,e]:= comp(y,$EmptyMode,e) or return nil
    [mOver,mUnder]:=
      modeIsAggregateOf("List",m,e) or return
        stackMessage('"mode: %1pb must be a list of other modes",[m])
    if null get(x,"mode",e) then [.,.,e]:=
      compMakeDeclaration(x,m,e) or return nil
    e:= put(x,"value",[genSomeVariable(),m,$noEnv],e)
    [y'',m'',e] := coerce([y',m,e], mOver) or return nil
    [["ON",x,y''],e]
  it is ["STEP",index,start,inc,:optFinal] =>
    checkVariableName index
    $formalArgList:= [index,:$formalArgList]
    --if all start/inc/end compile as small integers, then loop
    --is compiled as a small integer loop
    final':= nil
    (start':= comp(start,$SmallInteger,e)) and
      (inc':= comp(inc,$NonNegativeInteger,start'.env)) and
        (not (optFinal is [final]) or
          (final':= comp(final,$SmallInteger,inc'.env))) =>
            indexmode:=
              comp(start,$NonNegativeInteger,e) =>
                      $NonNegativeInteger
              $SmallInteger
            if null get(index,"mode",e) then [.,.,e]:=
              compMakeDeclaration(index,indexmode,
                (final' => final'.env; inc'.env)) or return nil
            e:= put(index,"value",[genSomeVariable(),indexmode,$noEnv],e)
            if final' then optFinal:= [final'.expr]
            [["ISTEP",index,start'.expr,inc'.expr,:optFinal],e]
    [start,.,e]:=
      comp(start,$Integer,e) or return
        stackMessage('"start value of index: %1b must be an integer",[start])
    [inc,.,e]:=
      comp(inc,$Integer,e) or return
        stackMessage('"index increment: %1b must be an integer",[inc])
    if optFinal is [final] then
      [final,.,e]:=
        comp(final,$Integer,e) or return
          stackMessage('"final value of index: %1b must be an integer",[final])
      optFinal:= [final]
    indexmode:=
      comp(CADDR it,$NonNegativeInteger,e) => $NonNegativeInteger
      $Integer
    if null get(index,"mode",e) then [.,.,e]:=
      compMakeDeclaration(index,indexmode,e) or return nil
    e:= put(index,"value",[genSomeVariable(),indexmode,$noEnv],e)
    [["STEP",index,start,inc,:optFinal],e]
  it is ["WHILE",p] =>
    [p',m,e]:=
      comp(p,$Boolean,e) or return
        stackMessage('"WHILE operand: %1b is not Boolean valued",[p])
    [["WHILE",p'],e]
  it is ["UNTIL",p] => ($until:= p; ['$until,e])
  it is ["|",x] =>
    u:=
      comp(x,$Boolean,e) or return
        stackMessage('"SUCHTHAT operand: %1b is not Boolean value",[x])
    [["|",u.expr],u.env]
  nil
 
--isAggregateMode(m,e) ==
--  m is [c,R] and MEMQ(c,'(Vector List)) => R
--  name:=
--    m is [fn,:.] => fn
--    m="$" => "Rep"
--    m
--  get(name,"value",e) is [c,R] and MEMQ(c,'(Vector List)) => R
 
modeIsAggregateOf(ListOrVector,m,e) ==
  m is [ =ListOrVector,R] => [m,R]
--m = '$EmptyMode => [m,m] I don't think this is correct, breaks POLY +
  m is ["Union",:l] =>
    mList:= [pair for m' in l | (pair:= modeIsAggregateOf(ListOrVector,m',e))]
    1=#mList => first mList
  name:=
    m is [fn,:.] => fn
    RepIfRepHack m
  get(name,"value",e) is [[ =ListOrVector,R],:.] => [m,R]
 
--% VECTOR ITERATORS
 
--the following 4 functions are not currently used
 
compCollectV(form,m,e) ==
  fn(form,[m,:$exitModeStack],[#$exitModeStack,:$leaveLevelStack],e) where
    fn(form,$exitModeStack,$leaveLevelStack,e) ==
      [repeatOrCollect,it,body]:= form
      [it',e]:= compIteratorV(it,e) or return nil
      m:= first $exitModeStack
      [mOver,mUnder]:= modeIsAggregateOf("Vector",m,e) or $EmptyMode
      [body',m',e']:= compOrCroak(body,mUnder,e) or return nil
      form':= ["COLLECTV",it',body']
      n:=
	it' is ["STEP",.,s,i,f] or it' is ["ISTEP",.,s,i,f] =>
	      computeMaxIndex(s,f,i);
	return nil
      coerce([form',mOver,e'],m)

compIteratorV(it,e) ==
  it is ["STEP",index,start,inc,final] =>
    (start':= comp(start,$Integer,e)) and
      (inc':= comp(inc,$NonNegativeInteger,start'.env)) and
       (final':= comp(final,$Integer,inc'.env)) =>
	indexmode:=
	  comp(start,$NonNegativeInteger,e) => $NonNegativeInteger
	  $Integer
	if null get(index,"mode",e) then [.,.,e]:=
	  compMakeDeclaration(index,indexmode,final'.env) or
	    return nil
	e:= put(index,"value",[genSomeVariable(),indexmode,$noEnv],e)
	[["ISTEP",index,start'.expr,inc'.expr,final'.expr],e]
    [start,.,e]:=
      comp(start,$Integer,e) or return
	stackMessage('"start value of index: %1b is not an integer",[start])
    [inc,.,e]:=
      comp(inc,$NonNegativeInteger,e) or return
	stackMessage('"index increment: %1b must be a non-negative integer",
          [inc])
    [final,.,e]:=
      comp(final,$Integer,e) or return
	stackMessage('"final value of index: %1b is not an integer",[final])
    indexmode:=
      comp(CADDR it,$NonNegativeInteger,e) => $NonNegativeInteger
      $Integer
    if null get(index,"mode",e) then [.,.,e]:=
      compMakeDeclaration(index,indexmode,e) or return nil
    e:= put(index,"value",[genSomeVariable(),indexmode,$noEnv],e)
    [["STEP",index,start,inc,final],e]
  nil

computeMaxIndex(s,f,i) ==
  i^=1 => cannotDo()
  s=1 => f
  exprDifference(f,exprDifference(s,1))

exprDifference(x,y) ==
  y=0 => x
  FIXP x and FIXP y => DIFFERENCE(x,y)
  ["DIFFERENCE",x,y]


--% rep/per morphisms

++ Compile the form `per x' under the mode `m'.
++ The `per' operator is active only for new-style definition for
++ representation domain.
compPer(["per",x],m,e) ==
  $useRepresentationHack => nil
  inType := getRepresentation e or return nil
  T := comp(x,inType,e) or return nil
  if $subdomain then
    T := 
      INTEGERP T.expr and satisfies(T.expr,domainVMPredicate "$") => 
        [T.expr,"$",e]
      coerceSuperset(T,"$") or return nil
  else 
    rplac(second T,"$")
  coerce(T,m)

++ Compile the form `rep x' under the mode `m'.
++ Like `per', the `rep' operator is active only for new-style 
++ definition for representation domain.
compRep(["rep",x],m,e) ==
  $useRepresentationHack => nil
  T := comp(x,"$",e) or return nil
  rplac(second T,getRepresentation e or return nil)
  coerce(T,m)

--%
--% Entry point to the compiler
--%

preprocessParseTree pt ==
  $postStack := []
  pf := parseTransform postTransform pt
  $postStack = nil => pf
  displayPreCompilationErrors()
  nil

++ Takes a parse tree `pt', typecheck it and compile it down 
++ to VM instructions.
compileParseTree pt ==
  pt = nil => nil
  CURSTRM: local := $OutputStream
  pf := preprocessParseTree pt
  pf = nil => nil       -- stop if preprocessing was a disaster.
  -- Don't go further if only preprocessing was requested.
  $PrintOnly =>
    FORMAT(true,'"~S   =====>~%",$currentLine)
    PRETTYPRINT pf
  -- Now start actual compilation.
  $x: local := nil         -- ???
  $m: local := nil         -- ???
  $s: local := nil         -- ???
  $exitModeStack: local := []    -- Used by the compiler proper
  -- We don't usually call the compiler to process interpreter
  -- input, however attempt to second guess nevertheless.
  if $InteractiveMode then
    processInteractive(pf,nil)
  else if T := compTopLevel(pf,$EmptyMode,$InteractiveFrame) then
    [.,.,$InteractiveFrame] := T
  TERPRI()


--%
--% Register compilers for special forms.
-- Those compilers are on the `SPECIAL' property of the corresponding
-- special form operator symbol.
for x in [["|", :"compSuchthat"],_
	  ["@", :"compAtSign"],_
	  [":", :"compColon"],_
	  ["::", :"compCoerce"],_
	  ["QUOTE", :"compQuote"],_
	  ["add", :"compAdd"],_
	  ["CAPSULE", :"compCapsule"],_
	  ["case", :"compCase"],_
	  ["CATEGORY", :"compCategory"],_
	  ["COLLECT", :"compRepeatOrCollect"],_
	  ["COLLECTV", :"compCollectV"],_
	  ["CONS", :"compCons"],_
	  ["construct", :"compConstruct"],_
	  ["DEF", :"compDefine"],_
	  ["elt", :"compElt"],_
	  ["Enumeration", :"compCat"],_
	  ["exit", :"compExit"],_
	  ["has", :"compHas"],_
	  ["IF", : "compIf"],_
          ["xor",: "compExclusiveOr"],_
	  ["import", :"compImport"],_
	  ["is", :"compIs"],_
	  ["Join", :"compJoin"],_
	  ["leave", :"compLeave"],_
	  ["%LET", :"compSetq"],_
	  ["MDEF", :"compMacro"],_
          ["not", :"compLogicalNot"],_
	  ["pretend", :"compPretend"],_
	  ["Record", :"compCat"],_
	  ["RecordCategory", :"compConstructorCategory"],_
	  ["REDUCE", :"compReduce"],_
	  ["REPEAT", :"compRepeatOrCollect"],_
	  ["return", :"compReturn"],_
	  ["SEQ", :"compSeq"],_
	  ["SETQ", :"compSetq"],_
	  ["SubDomain", :"compSubDomain"],_
	  ["SubsetCategory", :"compSubsetCategory"],_
	  ["Union", :"compCat"],_
	  ["Mapping", :"compCat"],_
	  ["UnionCategory", :"compConstructorCategory"],_
	  ["where", :"compWhere"],_
          ["per",:"compPer"],_
          ["rep",:"compRep"],_
          ["%Comma",:"compComma"],_
          ["%Match",:"compMatch"],_
          ["%SignatureImport",:"compSignatureImport"],_
          ["[||]", :"compileQuasiquote"]] repeat
  MAKEPROP(first x, "SPECIAL", rest x)
