;; Copyright (c) 1991-2002, The Numerical ALgorithms Group Ltd.
;; All rights reserved.
;; Copyright (C) 2007-2009, Gabriel Dos Reis.
;; All rights reserved.
;;
;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions are
;; met:
;;
;;     - Redistributions of source code must retain the above copyright
;;       notice, this list of conditions and the following disclaimer.
;;
;;     - Redistributions in binary form must reproduce the above copyright
;;       notice, this list of conditions and the following disclaimer in
;;       the documentation and/or other materials provided with the
;;       distribution.
;;
;;     - Neither the name of The Numerical ALgorithms Group Ltd. nor the
;;       names of its contributors may be used to endorse or promote products
;;       derived from this software without specific prior written permission.
;;
;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
;; IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
;; TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
;; PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
;; OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
;; EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
;; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
;; PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
;; LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
;; NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
;; SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


;; %       Scratchpad II Boot Language Grammar, Common Lisp Version
;; %       IBM Thomas J. Watson Research Center
;; %       Summer, 1986
;; %
;; %       NOTE: Substantially different from VM/LISP version, due to
;; %             different parser and attempt to render more within META proper.

;; .META(New NewExpr Process)
;; .PACKAGE 'BOOT'
;; .DECLARE(tmptok TOK ParseMode DEFINITION_NAME LABLASOC)
;; .PREFIX 'PARSE-'

;; NewExpr:        =')' .(processSynonyms) Command
;;               / .(SETQ DEFINITION_NAME (CURRENT-SYMBOL)) Statement ;

;; Command:        ')' SpecialKeyWord SpecialCommand +() ;

;; SpecialKeyWord: =(MATCH-CURRENT-TOKEN "IDENTIFIER)
;;                 .(SETF (TOKEN-SYMBOL (CURRENT-TOKEN)) (unAbbreviateKeyword (CURRENT-SYMBOL))) ;

;; SpecialCommand: 'show' <'?' / Expression>! +(show #1) CommandTail
;;               / ?(MEMBER (CURRENT-SYMBOL) \$noParseCommands)
;;                  .(FUNCALL (CURRENT-SYMBOL))
;;               / ?(MEMBER (CURRENT-SYMBOL) \$tokenCommands) TokenList
;;                   TokenCommandTail
;;               / PrimaryOrQM* CommandTail ;

;; TokenList:      (^?(isTokenDelimiter) +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN))* ;

;; TokenCommandTail:
;;                 <TokenOption*>! ?(atEndOfLine) +(#2 -#1) .(systemCommand #1) ;

;; TokenOption:    ')' TokenList ;

;; CommandTail:    <Option*>! ?(atEndOfLine) +(#2 -#1) .(systemCommand #1) ;

;; PrimaryOrQM:    '?' +\? / Primary ;

;; Option:         ')' PrimaryOrQM* ;

;; Statement:      Expr{0} <(',' Expr{0})* +(Series #2 -#1)>;

;; InfixWith:      With +(Join #2 #1) ;

;; With:           'with' Category +(with #1) ;

;; Category:      'if' Expression 'then' Category <'else' Category>! +(if #3 #2 #1)
;;               / '(' Category <(';' Category)*>! ')' +(CATEGORY #2 -#1)
;;               / .(SETQ $1 (LINE-NUMBER CURRENT-LINE)) Application 
;;                    ( ':' Expression +(Signature #2 #1)
;;                              .(recordSignatureDocumentation ##1 $1)
;;                            / +(Attribute #1)
;;                              .(recordAttributeDocumentation ##1 $1));

;; Expression:   Expr{(PARSE-rightBindingPowerOf (MAKE-SYMBOL-OF PRIOR-TOKEN) ParseMode)}
;;                 +#1 ;

;; Import:         'import' Expr{1000} <(',' Expr{1000})*>! +(import #2 -#1) ;

;; Infix:          =TRUE +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN) <TokTail>
;;                 Expression +(#2 #2 #1) ;

;; Prefix:         =TRUE +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN) <TokTail>
;;                 Expression +(#2 #1) ;

;; Suffix:         +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN) <TokTail> +(#1 #1) ;

;; TokTail:        ?(AND (EQ (CURRENT-SYMBOL) "\$)
;;                       (OR (ALPHA-CHAR-P (CURRENT-CHAR))
;;                           (CHAR-EQ (CURRENT-CHAR) '$')
;;                           (CHAR-EQ (CURRENT-CHAR) '\%')
;;                           (CHAR-EQ (CURRENT-CHAR) '(')))
;;                 .(SETQ $1 (COPY-TOKEN PRIOR-TOKEN)) Qualification
;;                 .(SETQ PRIOR-TOKEN $1) ;

;; Qualification:  '$' Primary1 +=(dollarTran #1 #1) ;

;; SemiColon:      ';' (Expr{82} / + \/throwAway) +(\; #2 #1) ;

;; Return:         'return' Expression +(return #1) ;

;; Exit:           'exit' (Expression / +\$NoValue) +(exit #1) ;

;; Leave:          'leave' ( Expression / +\$NoValue )
;;                 ('from' Label +(leaveFrom #1 #1) / +(leave #1)) ;

;; Seg:            GliphTok{"\.\.} <Expression>! +(SEGMENT #2 #1) ;

;; Conditional:    'if' Expression 'then' Expression <'else' ElseClause>!
;;                    +(if #3 #2 #1) ;

;; ElseClause:     ?(EQ (CURRENT-SYMBOL) "if) Conditional / Expression ;

;; Loop:           Iterator* 'repeat' Expr{110} +(REPEAT -#2 #1)
;;               / 'repeat' Expr{110} +(REPEAT #1) ;

;; Iterator:       'for' Primary 'in' Expression
;;                 ( 'by' Expr{200} +(INBY #3 #2 #1) / +(IN #2 #1) )
;;                 < '\|' Expr{111} +(\| #1) >
;;               / 'while' Expr{190} +(WHILE #1)
;;               / 'until' Expr{190} +(UNTIL #1) ;

;; Expr{RBP}:      NudPart{RBP} <LedPart{RBP}>* +#1;

;; LabelExpr:      Label Expr{120} +(LABEL #2 #1) ;

;; Label:          '<<' Name '>>' ;

;; LedPart{RBP}:   Operation{"Led RBP} +#1;

;; NudPart{RBP}:   (Operation{"Nud RBP} / Reduction / Form) +#1 ;

;; Operation{ParseMode RBP}:
;;         ^?(MATCH-CURRENT-TOKEN "IDENTIFIER)
;;         ?(GETL (SETQ tmptok (CURRENT-SYMBOL)) ParseMode)
;;         ?(LT RBP (PARSE-leftBindingPowerOf tmptok ParseMode))
;;         .(SETQ RBP (PARSE-rightBindingPowerOf tmptok ParseMode))
;;         getSemanticForm{tmptok ParseMode (ELEMN (GETL tmptok ParseMode) 5 NIL)} ;

;; % Binding powers stored under the Led and Red properties of an operator
;; % are set up by the file BOTTOMUP.LISP.  The format for a Led property
;; % is <Operator Left-Power Right-Power>, and the same for a Nud, except that
;; % it may also have a fourth component <Special-Handler>. ELEMN attempts to
;; % get the Nth indicator, counting from 1.

;; leftBindingPowerOf{X IND}: =(LET ((Y (GETL X IND))) (IF Y (ELEMN Y 3 0) 0)) ;

;; rightBindingPowerOf{X IND}: =(LET ((Y (GETL X IND))) (IF Y (ELEMN Y 4 105) 105)) ;

;; getSemanticForm{X IND Y}:
;;                 ?(AND Y (EVAL Y)) / ?(EQ IND "Nud) Prefix / ?(EQ IND "Led) Infix ;


;; Reduction:      ReductionOp Expr{1000} +(Reduce #2 #1) ;

;; ReductionOp:    ?(AND (GETL (CURRENT-SYMBOL) "Led)
;;                       (MATCH-NEXT-TOKEN "GLIPH "/)) % Forgive me!
;;                 +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN) .(ADVANCE-TOKEN) ;

;; Form:           'iterate' < 'from' Label +(#1) >! +(iterate -#1)
;;               / 'yield' Application +(yield #1)
;;               / Application ;

;; Application: Primary <Selector>* <Application +(#2 #1)>;

;; Selector: ?NONBLANK ?(EQ (CURRENT-SYMBOL) "\.) ?(CHAR-NE (CURRENT-CHAR) "\ )
;;                  '.' PrimaryNoFloat +(#2 #1)
;;           / (Float /'.' Primary) +(#2 #1);

;; PrimaryNoFloat: Primary1 <TokTail> ;

;; Primary: Float /PrimaryNoFloat ;

;; Primary1: VarForm <=(AND NONBLANK (EQ (CURRENT-SYMBOL) "\()) Primary1 +(#2 #1)>
;;         /Quad
;;         /String
;;         /IntegerTok
;;         /FormalParameter
;;         /='\'' ('\'' Expr{999} +(QUOTE #1))
;;         /Sequence
;;         /Enclosure ;

;; Float: FloatBase (?NONBLANK FloatExponent / +0) +=(MAKE-FLOAT #4 #2 #2 #1) ;

;; FloatBase: ?(FIXP (CURRENT-SYMBOL)) ?(CHAR-EQ (CURRENT-CHAR) '.')
;;            ?(CHAR-NE (NEXT-CHAR) '.')
;;               IntegerTok FloatBasePart
;;           /?(FIXP (CURRENT-SYMBOL)) ?(CHAR-EQ (CHAR-UPCASE (CURRENT-CHAR)) "E)
;;               IntegerTok +0 +0
;;          /?(DIGITP (CURRENT-CHAR)) ?(EQ (CURRENT-SYMBOL) "\.)
;;               +0 FloatBasePart ;

;; FloatBasePart: '.'
;;   (?(DIGITP (CURRENT-CHAR)) +=(TOKEN-NONBLANK (CURRENT-TOKEN)) IntegerTok
;;   / +0 +0);


;; FloatExponent: =(AND (MEMBER (CURRENT-SYMBOL) "(E e))
;;                      (FIND (CURRENT-CHAR) '+-'))
;;                  .(ADVANCE-TOKEN)
;;         (IntegerTok/'+' IntegerTok/'-' IntegerTok +=(MINUS #1)/+0)
;;        /?(IDENTP (CURRENT-SYMBOL)) =(SETQ $1 (FLOATEXPID (CURRENT-SYMBOL)))
;;        .(ADVANCE-TOKEN) +=$1 ;

;; Enclosure:      '(' ( Expr{6} ')' / ')' +(Tuple) )
;;               / '{' ( Expr{6} '}' +(brace (construct #1)) / '}' +(brace)) ;

;; IntegerTok:     NUMBER ;

;; FloatTok:       NUMBER +=(BFP- #1) ;

;; FormalParameter: FormalParameterTok ;

;; FormalParameterTok: ARGUMENT-DESIGNATOR ;

;; Quad:           '$' +\$ ;

;; String:         SPADSTRING ;

;; VarForm:        Name <Scripts +(Scripts #2 #1) > +#1 ;

;; Scripts:        ?NONBLANK '[' ScriptItem ']' ;

;; ScriptItem:     Expr{90} <(';' ScriptItem)* +(\; #2 -#1)>
;;               / ';' ScriptItem +(PrefixSC #1) ;

;; Name:           IDENTIFIER +#1 ;

;; Data:           .(SETQ LABLASOC NIL) Sexpr +(QUOTE =(TRANSLABEL #1 LABLASOC)) ;

;; Sexpr:          .(ADVANCE-TOKEN) Sexpr1 ;

;; Sexpr1:       AnyId
;;               < NBGliphTok{"\=} Sexpr1
;;                  .(SETQ LABLASOC (CONS (CONS #2 ##1) LABLASOC))>
;;               / '\'' Sexpr1 +(QUOTE #1)
;;               / IntegerTok
;;               / '-' IntegerTok +=(MINUS #1)
;;               / String
;;               / '<' <Sexpr1*>! '>' +=(LIST2VEC #1)
;;               / '(' <Sexpr1* <GliphTok{"\.} Sexpr1 +=(NCONC #2 #1)>>! ')' ;

;; NBGliphTok{tok}:   ?(AND (MATCH-CURRENT-TOKEN "GLIPH tok) NONBLANK)
;;                     .(ADVANCE-TOKEN) ;

;; GliphTok{tok}:     ?(MATCH-CURRENT-TOKEN "GLIPH tok) .(ADVANCE-TOKEN) ;

;; AnyId:          IDENTIFIER
;;               / (='$' +=(CURRENT-SYMBOL) .(ADVANCE-TOKEN) / KEYWORD) ;

;; Sequence:       OpenBracket Sequence1 ']'
;;               / OpenBrace Sequence1 '}' +(brace #1) ;

;; Sequence1:     (Expression +(#2 #1) / +(#1)) <IteratorTail +(COLLECT -#1 #1)>  ;

;; OpenBracket:    =(EQ (getToken (SETQ $1 (CURRENT-SYMBOL))) "\[ )
;;                       (=(EQCAR $1 "elt) +(elt =(CADR $1) construct)
;;                         / +construct) .(ADVANCE-TOKEN) ;

;; OpenBrace:      =(EQ (getToken (SETQ $1 (CURRENT-SYMBOL))) "\{ )
;;                       (=(EQCAR $1 "elt) +(elt =(CADR $1) brace)
;;                         / +construct) .(ADVANCE-TOKEN) ;

;; IteratorTail:   ('repeat' <Iterator*>! / Iterator*) ;

;; .FIN ;



(IMPORT-MODULE "parsing")
(IN-PACKAGE "BOOT" )


(DEFPARAMETER |tmptok| NIL)
(DEFPARAMETER TOK NIL)
(DEFPARAMETER |ParseMode| NIL)
(DEFPARAMETER DEFINITION_NAME NIL)
(DEFPARAMETER LABLASOC NIL)

(defun |isTokenDelimiter| () 
       (MEMBER (CURRENT-SYMBOL) '(\) END\_UNIT NIL)))


(DEFUN |PARSE-NewExpr| ()
  (OR (AND (MATCH-STRING ")") (ACTION (|processSynonyms|))
           (MUST (|PARSE-Command|)))
      (AND (ACTION (SETQ DEFINITION_NAME (CURRENT-SYMBOL)))
           (|PARSE-Statement|)))) 


(DEFUN |PARSE-Command| ()
  (AND (MATCH-ADVANCE-STRING ")") (MUST (|PARSE-SpecialKeyWord|))
       (MUST (|PARSE-SpecialCommand|))
       (PUSH-REDUCTION '|PARSE-Command| NIL))) 


(DEFUN |PARSE-SpecialKeyWord| ()
  (AND (MATCH-CURRENT-TOKEN 'IDENTIFIER)
       (ACTION (SETF (TOKEN-SYMBOL (CURRENT-TOKEN))
                     (|unAbbreviateKeyword| (CURRENT-SYMBOL)))))) 


(DEFUN |PARSE-SpecialCommand| ()
  (OR (AND (MATCH-ADVANCE-STRING "show")
           (BANG FIL_TEST
                 (OPTIONAL
                     (OR (MATCH-ADVANCE-STRING "?")
                         (|PARSE-Expression|))))
           (PUSH-REDUCTION '|PARSE-SpecialCommand|
               (CONS '|show| (CONS (POP-STACK-1) NIL)))
           (MUST (|PARSE-CommandTail|)))
      (AND (MEMBER (CURRENT-SYMBOL) |$noParseCommands|)
           (ACTION (FUNCALL (CURRENT-SYMBOL))))
      (AND (MEMBER (CURRENT-SYMBOL) |$tokenCommands|)
           (|PARSE-TokenList|) (MUST (|PARSE-TokenCommandTail|)))
      (AND (STAR REPEATOR (|PARSE-PrimaryOrQM|))
           (MUST (|PARSE-CommandTail|))))) 


(DEFUN |PARSE-TokenList| ()
  (STAR REPEATOR
        (AND (NOT (|isTokenDelimiter|))
             (PUSH-REDUCTION '|PARSE-TokenList| (CURRENT-SYMBOL))
             (ACTION (ADVANCE-TOKEN))))) 


(DEFUN |PARSE-TokenCommandTail| ()
  (AND (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-TokenOption|))))
       (|atEndOfLine|)
       (PUSH-REDUCTION '|PARSE-TokenCommandTail|
           (CONS (POP-STACK-2) (APPEND (POP-STACK-1) NIL)))
       (ACTION (|systemCommand| (POP-STACK-1))))) 


(DEFUN |PARSE-TokenOption| ()
  (AND (MATCH-ADVANCE-STRING ")") (MUST (|PARSE-TokenList|)))) 


(DEFUN |PARSE-CommandTail| ()
  (AND (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-Option|))))
       (|atEndOfLine|)
       (PUSH-REDUCTION '|PARSE-CommandTail|
           (CONS (POP-STACK-2) (APPEND (POP-STACK-1) NIL)))
       (ACTION (|systemCommand| (POP-STACK-1))))) 


(DEFUN |PARSE-PrimaryOrQM| ()
  (OR (AND (MATCH-ADVANCE-STRING "?")
           (PUSH-REDUCTION '|PARSE-PrimaryOrQM| '?))
      (|PARSE-Primary|))) 


(DEFUN |PARSE-Option| ()
  (AND (MATCH-ADVANCE-STRING ")")
       (MUST (STAR REPEATOR (|PARSE-PrimaryOrQM|))))) 


(DEFUN |PARSE-Statement| ()
  (AND (|PARSE-Expr| 0)
       (OPTIONAL
           (AND (STAR REPEATOR
                      (AND (MATCH-ADVANCE-STRING ",")
                           (MUST (|PARSE-Expr| 0))))
                (PUSH-REDUCTION '|PARSE-Statement|
                    (CONS '|Series|
                          (CONS (POP-STACK-2)
                                (APPEND (POP-STACK-1) NIL)))))))) 


(DEFUN |PARSE-InfixWith| ()
  (AND (|PARSE-With|)
       (PUSH-REDUCTION '|PARSE-InfixWith|
           (CONS '|Join| (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-With| ()
  (AND (MATCH-ADVANCE-STRING "with") (MUST (|PARSE-Category|))
       (PUSH-REDUCTION '|PARSE-With|
           (CONS '|with| (CONS (POP-STACK-1) NIL))))) 


(DEFUN |PARSE-Category| ()
  (PROG (G1)
    (RETURN
      (OR (AND (MATCH-ADVANCE-STRING "if") (MUST (|PARSE-Expression|))
               (MUST (MATCH-ADVANCE-STRING "then"))
               (MUST (|PARSE-Category|))
               (BANG FIL_TEST
                     (OPTIONAL
                         (AND (MATCH-ADVANCE-STRING "else")
                              (MUST (|PARSE-Category|)))))
               (PUSH-REDUCTION '|PARSE-Category|
                   (CONS '|if|
                         (CONS (POP-STACK-3)
                               (CONS (POP-STACK-2)
                                     (CONS (POP-STACK-1) NIL))))))
          (AND (MATCH-ADVANCE-STRING "(") (MUST (|PARSE-Category|))
               (BANG FIL_TEST
                     (OPTIONAL
                         (STAR REPEATOR
                               (AND (MATCH-ADVANCE-STRING ";")
                                    (MUST (|PARSE-Category|))))))
               (MUST (MATCH-ADVANCE-STRING ")"))
               (PUSH-REDUCTION '|PARSE-Category|
                   (CONS 'CATEGORY
                         (CONS (POP-STACK-2)
                               (APPEND (POP-STACK-1) NIL)))))
          (AND (ACTION (SETQ G1 (LINE-NUMBER CURRENT-LINE)))
               (OR (|PARSE-Application|)
		   (|PARSE-OperatorFunctionName|))
               (MUST (OR (AND (MATCH-ADVANCE-STRING ":")
                              (MUST (|PARSE-Expression|))
                              (PUSH-REDUCTION '|PARSE-Category|
                                  (CONS '|%Signature|
                                        (CONS (POP-STACK-2)
                                         (CONS (POP-STACK-1) NIL))))
                              (ACTION (|recordSignatureDocumentation|
                                       (NTH-STACK 1) G1)))
                         (AND (PUSH-REDUCTION '|PARSE-Category|
                                  (CONS '|%Attribute|
                                        (CONS (POP-STACK-1) NIL)))
                              (ACTION (|recordAttributeDocumentation|
                                       (NTH-STACK 1) G1)))))))))) 


(DEFUN |PARSE-Expression| ()
  (AND (|PARSE-Expr|
           (|PARSE-rightBindingPowerOf| (MAKE-SYMBOL-OF PRIOR-TOKEN)
               |ParseMode|))
       (PUSH-REDUCTION '|PARSE-Expression| (POP-STACK-1)))) 


(DEFUN |PARSE-Import| ()
  (AND (MATCH-ADVANCE-STRING "import") 
       (MUST (|PARSE-Expr| 1000))
       (OR (AND (MATCH-ADVANCE-STRING ":")
                (MUST (|PARSE-Expression|))
                (MUST (MATCH-ADVANCE-STRING "from"))
                (MUST (|PARSE-Expr| 1000))
                (PUSH-REDUCTION '|PARSE-Import|
		    (CONS '|%SignatureImport|
			  (CONS (POP-STACK-3)
				(CONS (POP-STACK-2)
				      (CONS (POP-STACK-1) NIL))))))
           (AND (BANG FIL_TEST
		      (OPTIONAL
		       (STAR REPEATOR
			     (AND (MATCH-ADVANCE-STRING ",")
				  (MUST (|PARSE-Expr| 1000))))))
		(PUSH-REDUCTION '|PARSE-Import|
                    (CONS '|import|
			  (CONS (POP-STACK-2) (APPEND (POP-STACK-1) NIL))))))))

;; domain inlining.  Same syntax as import directive; except
;; deliberate restriction on naming one type at a time.
;; -- gdr, 2009-02-28.
(DEFUN |PARSE-Inline| ()
  (AND (MATCH-ADVANCE-STRING "inline")
       (MUST (|PARSE-Expr| 1000))
       (PUSH-REDUCTION '|PARSE-Inline|
           (CONS '|%Inline| (CONS (POP-STACK-1) NIL)))))

;; quantified types.  At the moment, these are used only in
;; pattern-mathing cases.
;; -- gdr, 2009-06-14.
(DEFUN |PARSE-Scheme| ()
  (OR (AND (|PARSE-Quantifier|)
	   (MUST (|PARSE-QuantifiedVariableList|))
	   (MUST (MATCH-ADVANCE-STRING "."))
	   (MUST (|PARSE-Expr| 200))
	   (MUST (PUSH-REDUCTION '|PARSE-Forall|
				 (CONS (POP-STACK-3)
				       (CONS (POP-STACK-2)
					     (CONS (POP-STACK-1) NIL))))))
      (|PARSE-Application|)))

(DEFUN |PARSE-Quantifier| ()
  (OR (AND (MATCH-ADVANCE-STRING "forall")
	   (MUST (PUSH-REDUCTION '|PARSE-Quantifier| '|%Forall|)))
      (AND (MATCH-ADVANCE-STRING "exist")
	   (MUST (PUSH-REDUCTION '|PARSE-Quantifier| '|%Exist|)))))

(DEFUN |PARSE-QuantifiedVariableList| ()
  (AND (MATCH-ADVANCE-STRING "(")
       (MUST (|PARSE-QuantifiedVariable|))
       (OPTIONAL 
	(AND (STAR REPEATOR 
		   (AND (MATCH-ADVANCE-STRING ",")
			(MUST (|PARSE-QuantifiedVariable|))))
	     (PUSH-REDUCTION '|PARSE-QuantifiedVariableList|
			     (CONS '|%Sequence|
				   (CONS (POP-STACK-2) 
					 (APPEND (POP-STACK-1) NIL))))))
       (MUST (MATCH-ADVANCE-STRING ")"))))

(DEFUN |PARSE-QuantifiedVariable| ()
  (AND (PARSE-IDENTIFIER)
       (MUST (MATCH-ADVANCE-STRING ":"))
       (MUST (|PARSE-Application|))
       (MUST (PUSH-REDUCTION '|PARSE-QuantifiedVariable|
			     (CONS '|:| 
				   (CONS (POP-STACK-2)
					 (CONS (POP-STACK-1) NIL)))))))

(DEFUN |PARSE-Infix| ()
  (AND (PUSH-REDUCTION '|PARSE-Infix| (CURRENT-SYMBOL))
       (ACTION (ADVANCE-TOKEN)) (OPTIONAL (|PARSE-TokTail|))
       (MUST (|PARSE-Expression|))
       (PUSH-REDUCTION '|PARSE-Infix|
           (CONS (POP-STACK-2)
                 (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Prefix| ()
  (AND (PUSH-REDUCTION '|PARSE-Prefix| (CURRENT-SYMBOL))
       (ACTION (ADVANCE-TOKEN)) (OPTIONAL (|PARSE-TokTail|))
       (MUST (|PARSE-Expression|))
       (PUSH-REDUCTION '|PARSE-Prefix|
           (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))) 


(DEFUN |PARSE-Suffix| ()
  (AND (PUSH-REDUCTION '|PARSE-Suffix| (CURRENT-SYMBOL))
       (ACTION (ADVANCE-TOKEN)) (OPTIONAL (|PARSE-TokTail|))
       (PUSH-REDUCTION '|PARSE-Suffix|
           (CONS (POP-STACK-1) (CONS (POP-STACK-1) NIL))))) 


(DEFUN |PARSE-TokTail| ()
  (PROG (G1)
    (RETURN
      (AND (EQ (CURRENT-SYMBOL) '$)
           (OR (ALPHA-CHAR-P (CURRENT-CHAR))
               (CHAR-EQ (CURRENT-CHAR) "$")
               (CHAR-EQ (CURRENT-CHAR) "%")
               (CHAR-EQ (CURRENT-CHAR) "("))
           (ACTION (SETQ G1 (COPY-TOKEN PRIOR-TOKEN)))
           (|PARSE-Qualification|) (ACTION (SETQ PRIOR-TOKEN G1)))))) 


(DEFUN |PARSE-Qualification| ()
  (AND (MATCH-ADVANCE-STRING "$") (MUST (|PARSE-Primary1|))
       (PUSH-REDUCTION '|PARSE-Qualification|
           (|dollarTran| (POP-STACK-1) (POP-STACK-1))))) 


(DEFUN |PARSE-SemiColon| ()
  (AND (MATCH-ADVANCE-STRING ";")
       (MUST (OR (|PARSE-Expr| 82)
                 (PUSH-REDUCTION '|PARSE-SemiColon| '|/throwAway|)))
       (PUSH-REDUCTION '|PARSE-SemiColon|
           (CONS '|;| (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Return| ()
  (AND (MATCH-ADVANCE-STRING "return") (MUST (|PARSE-Expression|))
       (PUSH-REDUCTION '|PARSE-Return|
           (CONS '|return| (CONS (POP-STACK-1) NIL))))) 


(DEFUN |PARSE-Exit| ()
  (AND (MATCH-ADVANCE-STRING "exit")
       (MUST (OR (|PARSE-Expression|)
                 (PUSH-REDUCTION '|PARSE-Exit| '|$NoValue|)))
       (PUSH-REDUCTION '|PARSE-Exit|
           (CONS '|exit| (CONS (POP-STACK-1) NIL))))) 


(DEFUN |PARSE-Leave| ()
  (AND (MATCH-ADVANCE-STRING "leave")
       (MUST (OR (|PARSE-Expression|)
                 (PUSH-REDUCTION '|PARSE-Leave| '|$NoValue|)))
       (MUST (OR (AND (MATCH-ADVANCE-STRING "from")
                      (MUST (|PARSE-Label|))
                      (PUSH-REDUCTION '|PARSE-Leave|
                          (CONS '|leaveFrom|
                                (CONS (POP-STACK-1)
                                      (CONS (POP-STACK-1) NIL)))))
                 (PUSH-REDUCTION '|PARSE-Leave|
                     (CONS '|leave| (CONS (POP-STACK-1) NIL))))))) 


(DEFUN |PARSE-Seg| ()
  (AND (|PARSE-GliphTok| '|..|)
       (BANG FIL_TEST (OPTIONAL (|PARSE-Expression|)))
       (PUSH-REDUCTION '|PARSE-Seg|
           (CONS 'SEGMENT
                 (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Conditional| ()
  (AND (MATCH-ADVANCE-STRING "if") (MUST (|PARSE-Expression|))
       (MUST (MATCH-ADVANCE-STRING "then")) (MUST (|PARSE-Expression|))
       (BANG FIL_TEST
             (OPTIONAL
                 (AND (MATCH-ADVANCE-STRING "else")
                      (MUST (|PARSE-ElseClause|)))))
       (PUSH-REDUCTION '|PARSE-Conditional|
           (CONS '|if|
                 (CONS (POP-STACK-3)
                       (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))))) 


(DEFUN |PARSE-ElseClause| ()
  (OR (AND (EQ (CURRENT-SYMBOL) '|if|) (|PARSE-Conditional|))
      (|PARSE-Expression|))) 


(DEFUN |PARSE-Loop| ()
  (OR (AND (STAR REPEATOR (|PARSE-Iterator|))
           (MUST (MATCH-ADVANCE-STRING "repeat"))
           (MUST (|PARSE-Expr| 110))
           (PUSH-REDUCTION '|PARSE-Loop|
               (CONS 'REPEAT
                     (APPEND (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))
      (AND (MATCH-ADVANCE-STRING "repeat") (MUST (|PARSE-Expr| 110))
           (PUSH-REDUCTION '|PARSE-Loop|
               (CONS 'REPEAT (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Iterator| ()
  (OR (AND (MATCH-ADVANCE-STRING "for") (MUST (|PARSE-Primary|))
           (MUST (MATCH-ADVANCE-STRING "in"))
           (MUST (|PARSE-Expression|))
           (MUST (OR (AND (MATCH-ADVANCE-STRING "by")
                          (MUST (|PARSE-Expr| 200))
                          (PUSH-REDUCTION '|PARSE-Iterator|
                              (CONS 'INBY
                                    (CONS (POP-STACK-3)
                                     (CONS (POP-STACK-2)
                                      (CONS (POP-STACK-1) NIL))))))
                     (PUSH-REDUCTION '|PARSE-Iterator|
                         (CONS 'IN
                               (CONS (POP-STACK-2)
                                     (CONS (POP-STACK-1) NIL))))))
           (OPTIONAL
               (AND (MATCH-ADVANCE-STRING "|")
                    (MUST (|PARSE-Expr| 111))
                    (PUSH-REDUCTION '|PARSE-Iterator|
                        (CONS '|\|| (CONS (POP-STACK-1) NIL))))))
      (AND (MATCH-ADVANCE-STRING "while") (MUST (|PARSE-Expr| 190))
           (PUSH-REDUCTION '|PARSE-Iterator|
               (CONS 'WHILE (CONS (POP-STACK-1) NIL))))
      (AND (MATCH-ADVANCE-STRING "until") (MUST (|PARSE-Expr| 190))
           (PUSH-REDUCTION '|PARSE-Iterator|
               (CONS 'UNTIL (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Match| ()
  (AND (MATCH-ADVANCE-STRING "case")
       (MUST (|PARSE-Expr| 400))
       (MATCH-ADVANCE-STRING "is")
       (MUST (|PARSE-Expr| 110))
       (PUSH-REDUCTION '|PARSE-Match|
		       (CONS '|%Match|
			     (CONS (POP-STACK-2)
				   (CONS (POP-STACK-1) NIL))))))

(DEFUN |PARSE-Expr| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (|PARSE-NudPart| RBP)
       (OPTIONAL (STAR OPT_EXPR (|PARSE-LedPart| RBP)))
       (PUSH-REDUCTION '|PARSE-Expr| (POP-STACK-1)))) 


(DEFUN |PARSE-LabelExpr| ()
  (AND (|PARSE-Label|) (MUST (|PARSE-Expr| 120))
       (PUSH-REDUCTION '|PARSE-LabelExpr|
           (CONS 'LABEL (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Label| ()
  (AND (MATCH-ADVANCE-STRING "<<") (MUST (|PARSE-Name|))
       (MUST (MATCH-ADVANCE-STRING ">>")))) 


(DEFUN |PARSE-LedPart| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (|PARSE-Operation| '|Led| RBP)
       (PUSH-REDUCTION '|PARSE-LedPart| (POP-STACK-1)))) 


(DEFUN |PARSE-NudPart| (RBP)
  (DECLARE (SPECIAL RBP))
  (AND (OR (|PARSE-Operation| '|Nud| RBP) (|PARSE-Reduction|)
           (|PARSE-Form|))
       (PUSH-REDUCTION '|PARSE-NudPart| (POP-STACK-1)))) 


(DEFUN |PARSE-Operation| (|ParseMode| RBP)
  (DECLARE (SPECIAL |ParseMode| RBP))
  (AND (NOT (MATCH-CURRENT-TOKEN 'IDENTIFIER))
       (GETL (SETQ |tmptok| (CURRENT-SYMBOL)) |ParseMode|)
       (LT RBP (|PARSE-leftBindingPowerOf| |tmptok| |ParseMode|))
       (ACTION (SETQ RBP
                     (|PARSE-rightBindingPowerOf| |tmptok| |ParseMode|)))
       (|PARSE-getSemanticForm| |tmptok| |ParseMode|
           (ELEMN (GETL |tmptok| |ParseMode|) 5 NIL)))) 


(DEFUN |PARSE-leftBindingPowerOf| (X IND)
  (DECLARE (SPECIAL X IND))
  (LET ((Y (GETL X IND))) (IF Y (ELEMN Y 3 0) 0))) 


(DEFUN |PARSE-rightBindingPowerOf| (X IND)
  (DECLARE (SPECIAL X IND))
  (LET ((Y (GETL X IND))) (IF Y (ELEMN Y 4 105) 105))) 


(DEFUN |PARSE-getSemanticForm| (X IND Y)
  (DECLARE (SPECIAL X IND Y))
  (OR (AND Y (EVAL Y)) (AND (EQ IND '|Nud|) (|PARSE-Prefix|))
      (AND (EQ IND '|Led|) (|PARSE-Infix|)))) 


(DEFUN |PARSE-Reduction| ()
  (AND (|PARSE-ReductionOp|) (MUST (|PARSE-Expr| 1000))
       (PUSH-REDUCTION '|PARSE-Reduction|
           (CONS '|%Reduce|
                 (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-ReductionOp| ()
  (AND (GETL (CURRENT-SYMBOL) '|Led|)
       (MATCH-NEXT-TOKEN 'GLIPH '/)
       (PUSH-REDUCTION '|PARSE-ReductionOp| (CURRENT-SYMBOL))
       (ACTION (ADVANCE-TOKEN)) (ACTION (ADVANCE-TOKEN)))) 


(DEFUN |PARSE-Form| ()
  (OR (AND (MATCH-ADVANCE-STRING "iterate")
           (BANG FIL_TEST
                 (OPTIONAL
                     (AND (MATCH-ADVANCE-STRING "from")
                          (MUST (|PARSE-Label|))
                          (PUSH-REDUCTION '|PARSE-Form|
                              (CONS (POP-STACK-1) NIL)))))
           (PUSH-REDUCTION '|PARSE-Form|
               (CONS '|iterate| (APPEND (POP-STACK-1) NIL))))
      (AND (MATCH-ADVANCE-STRING "yield") (MUST (|PARSE-Application|))
           (PUSH-REDUCTION '|PARSE-Form|
               (CONS '|yield| (CONS (POP-STACK-1) NIL))))
      (|PARSE-Application|))) 


(DEFUN |PARSE-Application| ()
  (AND (|PARSE-Primary|) (OPTIONAL (STAR OPT_EXPR (|PARSE-Selector|)))
       (OPTIONAL
           (AND (|PARSE-Application|)
                (PUSH-REDUCTION '|PARSE-Application|
                    (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))))) 


(DEFUN |PARSE-Selector| ()
  (OR (AND NONBLANK (EQ (CURRENT-SYMBOL) '|.|)
           (CHAR-NE (CURRENT-CHAR) '| |) (MATCH-ADVANCE-STRING ".")
           (MUST (|PARSE-PrimaryNoFloat|))
           (MUST (PUSH-REDUCTION '|PARSE-Selector|
                     (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL)))))
      (AND (OR (|PARSE-Float|)
               (AND (MATCH-ADVANCE-STRING ".")
                    (MUST (|PARSE-Primary|))))
           (MUST (PUSH-REDUCTION '|PARSE-Selector|
                     (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))))) 


(DEFUN |PARSE-PrimaryNoFloat| ()
  (AND (|PARSE-Primary1|) (OPTIONAL (|PARSE-TokTail|)))) 


(DEFUN |PARSE-Primary| ()
  (OR (|PARSE-Float|) (|PARSE-PrimaryNoFloat|))) 


(DEFUN |PARSE-Primary1| ()
  (OR (AND (|PARSE-VarForm|)
           (OPTIONAL
               (AND NONBLANK (EQ (CURRENT-SYMBOL) '|(|)
                    (MUST (|PARSE-Primary1|))
                    (PUSH-REDUCTION '|PARSE-Primary1|
                        (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))))
      (|PARSE-Quad|) (|PARSE-String|) (|PARSE-IntegerTok|)
      (|PARSE-FormalParameter|)
      (AND (MATCH-ADVANCE-STRING "'")
           (MUST (AND (MUST (|PARSE-Data|))
                      (PUSH-REDUCTION '|PARSE-Primary1| (POP-STACK-1)))))
      (|PARSE-Sequence|) (|PARSE-Enclosure|))) 


(DEFUN |PARSE-Float| ()
  (AND (|PARSE-FloatBase|)
       (MUST (OR (AND NONBLANK (|PARSE-FloatExponent|))
                 (PUSH-REDUCTION '|PARSE-Float| 0)))
       (PUSH-REDUCTION '|PARSE-Float|
           (MAKE-FLOAT (POP-STACK-4) (POP-STACK-2) (POP-STACK-2)
               (POP-STACK-1))))) 


(DEFUN |PARSE-FloatBase| ()
  (OR (AND (FIXP (CURRENT-SYMBOL)) (CHAR-EQ (CURRENT-CHAR) ".")
           (CHAR-NE (NEXT-CHAR) ".") (|PARSE-IntegerTok|)
           (MUST (|PARSE-FloatBasePart|)))
      (AND (FIXP (CURRENT-SYMBOL))
           (CHAR-EQ (CHAR-UPCASE (CURRENT-CHAR)) 'E)
           (|PARSE-IntegerTok|) (PUSH-REDUCTION '|PARSE-FloatBase| 0)
           (PUSH-REDUCTION '|PARSE-FloatBase| 0))
      (AND (DIGITP (CURRENT-CHAR)) (EQ (CURRENT-SYMBOL) '|.|)
           (PUSH-REDUCTION '|PARSE-FloatBase| 0)
           (|PARSE-FloatBasePart|)))) 


(DEFUN |PARSE-FloatBasePart| ()
  (AND (MATCH-ADVANCE-STRING ".")
       (MUST (OR (AND (DIGITP (CURRENT-CHAR))
                      (PUSH-REDUCTION '|PARSE-FloatBasePart|
                          (TOKEN-NONBLANK (CURRENT-TOKEN)))
                      (|PARSE-IntegerTok|))
                 (AND (PUSH-REDUCTION '|PARSE-FloatBasePart| 0)
                      (PUSH-REDUCTION '|PARSE-FloatBasePart| 0)))))) 


(DEFUN |PARSE-FloatExponent| ()
  (PROG (G1)
    (RETURN
      (OR (AND (MEMBER (CURRENT-SYMBOL) '(E |e|))
               (FIND (CURRENT-CHAR) "+-") (ACTION (ADVANCE-TOKEN))
               (MUST (OR (|PARSE-IntegerTok|)
                         (AND (MATCH-ADVANCE-STRING "+")
                              (MUST (|PARSE-IntegerTok|)))
                         (AND (MATCH-ADVANCE-STRING "-")
                              (MUST (|PARSE-IntegerTok|))
                              (PUSH-REDUCTION '|PARSE-FloatExponent|
                                  (MINUS (POP-STACK-1))))
                         (PUSH-REDUCTION '|PARSE-FloatExponent| 0))))
          (AND (IDENTP (CURRENT-SYMBOL))
               (SETQ G1 (FLOATEXPID (CURRENT-SYMBOL)))
               (ACTION (ADVANCE-TOKEN))
               (PUSH-REDUCTION '|PARSE-FloatExponent| G1)))))) 


(DEFUN |PARSE-Enclosure| ()
  (OR (AND (MATCH-ADVANCE-STRING "(")
           (MUST (OR (AND (|PARSE-Expr| 6)
                          (MUST (MATCH-ADVANCE-STRING ")")))
                     (AND (MATCH-ADVANCE-STRING ")")
                          (PUSH-REDUCTION '|PARSE-Enclosure|
                              (CONS '|%Comma| NIL))))))
      (AND (MATCH-ADVANCE-STRING "{")
           (MUST (OR (AND (|PARSE-Expr| 6)
                          (MUST (MATCH-ADVANCE-STRING "}"))
                          (PUSH-REDUCTION '|PARSE-Enclosure|
                              (CONS '|brace|
                                    (CONS
                                     (CONS '|construct|
                                      (CONS (POP-STACK-1) NIL))
                                     NIL))))
                     (AND (MATCH-ADVANCE-STRING "}")
                          (PUSH-REDUCTION '|PARSE-Enclosure|
                              (CONS '|brace| NIL))))))
      (AND (MATCH-ADVANCE-STRING "[|")
	   (MUST (AND (|PARSE-Statement|)
		      (MUST (MATCH-ADVANCE-STRING "|]"))
		      (PUSH-REDUCTION '|PARSE-Enclosure|
				      (CONS '|[\|\|]|
					    (CONS (POP-STACK-1) NIL)))
		      )))
      )) 


(DEFUN |PARSE-IntegerTok| () (PARSE-NUMBER)) 


(DEFUN |PARSE-FloatTok| ()
  (AND (PARSE-NUMBER)
       (PUSH-REDUCTION '|PARSE-FloatTok| (POP-STACK-1)))) 


(DEFUN |PARSE-FormalParameter| () (|PARSE-FormalParameterTok|)) 


(DEFUN |PARSE-FormalParameterTok| () (PARSE-ARGUMENT-DESIGNATOR)) 


(DEFUN |PARSE-Quad| ()
  (AND (MATCH-ADVANCE-STRING "$")
       (PUSH-REDUCTION '|PARSE-Quad| '$)))


(DEFUN |PARSE-String| () (PARSE-SPADSTRING)) 


(DEFUN |PARSE-VarForm| ()
  (AND (|PARSE-Name|)
       (OPTIONAL
           (AND (|PARSE-Scripts|)
                (PUSH-REDUCTION '|PARSE-VarForm|
                    (CONS '|Scripts|
                          (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))))
       (PUSH-REDUCTION '|PARSE-VarForm| (POP-STACK-1)))) 


(DEFUN |PARSE-Scripts| ()
  (AND NONBLANK (MATCH-ADVANCE-STRING "[") (MUST (|PARSE-ScriptItem|))
       (MUST (MATCH-ADVANCE-STRING "]")))) 


(DEFUN |PARSE-ScriptItem| ()
  (OR (AND (|PARSE-Expr| 90)
           (OPTIONAL
               (AND (STAR REPEATOR
                          (AND (MATCH-ADVANCE-STRING ";")
                               (MUST (|PARSE-ScriptItem|))))
                    (PUSH-REDUCTION '|PARSE-ScriptItem|
                        (CONS '|;|
                              (CONS (POP-STACK-2)
                                    (APPEND (POP-STACK-1) NIL)))))))
      (AND (MATCH-ADVANCE-STRING ";") (MUST (|PARSE-ScriptItem|))
           (PUSH-REDUCTION '|PARSE-ScriptItem|
               (CONS '|PrefixSC| (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Name| ()
  (AND (PARSE-IDENTIFIER) (PUSH-REDUCTION '|PARSE-Name| (POP-STACK-1)))) 


(DEFUN |PARSE-Data| ()
  (AND (ACTION (SETQ LABLASOC NIL)) (|PARSE-Sexpr|)
       (PUSH-REDUCTION '|PARSE-Data|
           (CONS 'QUOTE (CONS (TRANSLABEL (POP-STACK-1) LABLASOC) NIL))))) 


(DEFUN |PARSE-Sexpr| ()
  (AND (ACTION (ADVANCE-TOKEN)) (|PARSE-Sexpr1|))) 


(DEFUN |PARSE-Sexpr1| ()
  (OR (|PARSE-IntegerTok|)
      (|PARSE-String|)
      (AND (|PARSE-AnyId|)
           (OPTIONAL
               (AND (|PARSE-NBGliphTok| '=) (MUST (|PARSE-Sexpr1|))
                    (ACTION (SETQ LABLASOC
                                  (CONS (CONS (POP-STACK-2)
                                         (NTH-STACK 1))
                                        LABLASOC))))))
      (AND (MATCH-ADVANCE-STRING "'") (MUST (|PARSE-Sexpr1|))
           (PUSH-REDUCTION '|PARSE-Sexpr1|
               (CONS 'QUOTE (CONS (POP-STACK-1) NIL))))
      ;; next form disabled -- gdr, 2009-06-15.
;      (AND (MATCH-ADVANCE-STRING "-") (MUST (|PARSE-IntegerTok|))
;           (PUSH-REDUCTION '|PARSE-Sexpr1| (MINUS (POP-STACK-1))))
      (AND (MATCH-ADVANCE-STRING "[")
           (BANG FIL_TEST (OPTIONAL (STAR REPEATOR (|PARSE-Sexpr1|))))
           (MUST (MATCH-ADVANCE-STRING "]"))
           (PUSH-REDUCTION '|PARSE-Sexpr1| (LIST2VEC (POP-STACK-1))))
      (AND (MATCH-ADVANCE-STRING "(")
           (BANG FIL_TEST
                 (OPTIONAL
                     (AND (STAR REPEATOR (|PARSE-Sexpr1|))
                          (OPTIONAL
                              (AND (|PARSE-GliphTok| '|.|)
                                   (MUST (|PARSE-Sexpr1|))
                                   (PUSH-REDUCTION '|PARSE-Sexpr1|
                                    (NCONC (POP-STACK-2) (POP-STACK-1))))))))
           (MUST (MATCH-ADVANCE-STRING ")"))))) 


(DEFUN |PARSE-NBGliphTok| (|tok|)
  (DECLARE (SPECIAL |tok|))
  (AND (MATCH-CURRENT-TOKEN 'GLIPH |tok|) NONBLANK
       (ACTION (ADVANCE-TOKEN)))) 


(DEFUN |PARSE-GliphTok| (|tok|)
  (DECLARE (SPECIAL |tok|))
  (AND (MATCH-CURRENT-TOKEN 'GLIPH |tok|) (ACTION (ADVANCE-TOKEN)))) 


(DEFUN |PARSE-AnyId| ()
  (OR (PARSE-IDENTIFIER)
      (OR (AND (MATCH-STRING "$")
               (PUSH-REDUCTION '|PARSE-AnyId| (CURRENT-SYMBOL))
               (ACTION (ADVANCE-TOKEN)))
          (PARSE-KEYWORD)
	  (|PARSE-OperatorFunctionName|))))


(DEFUN |PARSE-Sequence| ()
  (OR (AND (|PARSE-OpenBracket|) (MUST (|PARSE-Sequence1|))
           (MUST (MATCH-ADVANCE-STRING "]")))
      (AND (|PARSE-OpenBrace|) (MUST (|PARSE-Sequence1|))
           (MUST (MATCH-ADVANCE-STRING "}"))
           (PUSH-REDUCTION '|PARSE-Sequence|
               (CONS '|brace| (CONS (POP-STACK-1) NIL)))))) 


(DEFUN |PARSE-Sequence1| ()
  (AND (OR (AND (|PARSE-Expression|)
                (PUSH-REDUCTION '|PARSE-Sequence1|
                    (CONS (POP-STACK-2) (CONS (POP-STACK-1) NIL))))
           (PUSH-REDUCTION '|PARSE-Sequence1| (CONS (POP-STACK-1) NIL)))
       (OPTIONAL
           (AND (|PARSE-IteratorTail|)
                (PUSH-REDUCTION '|PARSE-Sequence1|
                    (CONS 'COLLECT
                          (APPEND (POP-STACK-1)
                                  (CONS (POP-STACK-1) NIL)))))))) 


(DEFUN |PARSE-OpenBracket| ()
  (PROG (G1)
    (RETURN
      (AND (EQ (|getToken| (SETQ G1 (CURRENT-SYMBOL))) '[)
           (MUST (OR (AND (EQCAR G1 '|elt|)
                          (PUSH-REDUCTION '|PARSE-OpenBracket|
                              (CONS '|elt|
                                    (CONS (CADR G1)
                                     (CONS '|construct| NIL)))))
                     (PUSH-REDUCTION '|PARSE-OpenBracket| '|construct|)))
           (ACTION (ADVANCE-TOKEN)))))) 


(DEFUN |PARSE-OpenBrace| ()
  (PROG (G1)
    (RETURN
      (AND (EQ (|getToken| (SETQ G1 (CURRENT-SYMBOL))) '{)
           (MUST (OR (AND (EQCAR G1 '|elt|)
                          (PUSH-REDUCTION '|PARSE-OpenBrace|
                              (CONS '|elt|
                                    (CONS (CADR G1)
                                     (CONS '|brace| NIL)))))
                     (PUSH-REDUCTION '|PARSE-OpenBrace| '|construct|)))
           (ACTION (ADVANCE-TOKEN)))))) 


(DEFUN |PARSE-IteratorTail| ()
  (OR (AND (MATCH-ADVANCE-STRING "repeat")
           (BANG FIL_TEST
                 (OPTIONAL (STAR REPEATOR (|PARSE-Iterator|)))))
      (STAR REPEATOR (|PARSE-Iterator|)))) 

