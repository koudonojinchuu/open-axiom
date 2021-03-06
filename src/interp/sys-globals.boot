-- Copyright (c) 1991-2002, The Numerical Algorithms Group Ltd.
-- All rights reserved.
-- Copyright (C) 2007-2016, Gabriel Dos Reis.
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
--

--
-- This file collects and documents some the global variables used by either
-- the interpreter or the compiler or both.
--

import hash
import sys_-constants
namespace BOOT

++ FIXME
$saturn := false

++ FIXME
$SPAD__ERRORS := VECTOR(0, 0, 0)

++
$abbreviationTable := nil

++
$bootStrapMode := false

++
$BreakMode := "query"

++
$cacheAlist := nil
$cacheCount := 0

++
$createUpdateFiles := false

++
$currentLine := nil

++
$domainTraceNameAssoc := []

++
$exitModeStack := []

++
$IOindex := 0

++
$inputPromptType := "step"

++
$whereList := []

++
$warningStack := []

++
$form := nil

++
$fromSpadTrace := false

++
$genSDVar := 0

++
$Index := 0

++
$inLispVM := true

++
$insideCapsuleFunctionIfTrue := false

++
$insideCoerceInteractiveHardIfTrue := false

++
$insideConstructIfTrue := false

++
$insideExpressionIfTrue := false

++
$insideFunctorIfTrue := false

++
$insideWhereIfTrue := false

++
$InteractiveFrame := $EmptyEnvironment
$e := $EmptyEnvironment
$env := [[nil]]

++
$InteractiveTimingStatsIfTrue := false

++
$forceDatabaseUpdate := false

++
$leaveLevelStack := []

++
++ FIXME: eventually move to trace.boot.
$letAssoc := false

++
$lisplibOperationAlist := []

++
$mapSubNameAlist := []

++
$mathTrace := false

++
$mathTraceList := []

++
$prefix := nil

++ FIXME: Eventually move to comp.lisp.pamphlet
$PrettyPrint := false

++
$previousTime := 0

++
$useBFasDefault := true

++
$semanticErrorStack := []

++
++ FIXME: Eventually move to compiler.boot.pamphlet.
$reportExitModeStack := false

++
$tracedModemap := nil

++
$tracedSpadModemap := nil

++
$traceletFunctions := []

++
$useDCQnotLET := false

++
$TranslateOnly := false

++
$topOp := nil

++
$streamCount := 0

++
$FUNCTION := nil

++
$FUNNAME := nil

++
$FUNNAME__TAIL := '(())

++
$LINESTACK := "BEGIN__UNIT"

++
$MAXLINENUMBER := 0

++
$OLDLINE := nil

++
$PrintOnly := false

++
$QuickLet := true

++
$reportBottomUpFlag := false

++
$reportFlag := false

++
$returnMode := $EmptyMode

++
$SetFunctions := nil

++
++ FIXME: Eventually remove.
$sourceFileTypes := ["SPAD"]

++
++ If true, make the system verbose about object files being loaded
$printLoadMsgs := false

++
$reportCompilation := false

++
$CategoryFrame :=
  '((((Category (modemap (((Category) (Category)) (T *))))_
      (Join (modemap (((Category) (Category) (Category)) (T *))_
                     (((Category) (Category) (List Category)) (T *)))))))

++
$spadLibFT := "NRLIB"

++ true if we are compiling a function.
$compilingMap := false

++
$TRACELETFLAG := false

++
$NEWSPAD := false

++
$insideCoerceInteractive := false

++
$insideEvalMmCondIfTrue := false

++
$UserLevel := "development"

++
$DIRECTORY_-LIST := []

++
$LIBRARY_-DIRECTORY_-LIST := []

++
$byConstructors := nil

++
$constructorsSeen := nil

++
$docList := []

++
$headerDocumentation := nil

++
$constructorLineNumber := 0

++
$maxSignatureLineNumber := 0

SPADERRORSTREAM := _*ERROR_-OUTPUT_*

++
_/VERSION := 0
_/WSNAME := "NOBOOT"

++
LINE := nil

$Echo := false

++ answers x has y category questions
$HasCategoryTable := nil

++
_*BUILD_-VERSION_* := nil
_*YEARWEEK_* := nil

++
_/TRACENAMES := nil

++
$highlightAllowed := true

++
SETQ(_*PRINT_-CIRCLE_*, true)
SETQ(_*PRINT_-ARRAY_*, false)
SETQ(_*PRINT_-PRETTY_*, true)

++
INPUT_-LIBRARIES := nil
OUTPUT_-LIBRARY := nil

++
$editFile := nil

++
$newConlist := nil

++ True if the input file uses old semantics of `Rep',
++ e.g. implicit equivalent Rep <-> % with capsules.  
++ This semenatics is in effect only when `Rep' is defined
++ through assignment.
$useRepresentationHack := true

++
$insideCanCoerceFrom := nil

++
$sourceFiles := []

++ ???
$x := nil
$f := nil
$m := nil

++ ???
_/SOURCEFILES := []
_/SPACELIST := []

$categoryPredicateList := []

$getDomainCode := nil
$addForm := nil

$compForModeIfTrue := false

--%

$algebraOutputStream := forkStreamByName "*STANDARD-OUTPUT*"

++
$texOutputStream := forkStreamByName "*STANDARD-OUTPUT*"

$fortranOutputStream := forkStreamByName "*STANDARD-OUTPUT*"

--%

++ True if we are building the system algebra.
$buildingSystemAlgebra := false

++ if true, then the interpreter or compiler should inform about 
++ code generation, etc.
$verbose := true

++ True if we should consider the representation domain (`Rep')
++ as candidate for inlining, for the purpose of reducing
++ abstraction penalty.
$optimizeRep := false

++
$leanMode := false
