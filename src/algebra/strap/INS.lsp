
(/VERSIONCHECK 2) 

(DEFPARAMETER |IntegerNumberSystem;AL| 'NIL) 

(DEFUN |IntegerNumberSystem| ()
  (LET (#:G1412)
    (COND
      (|IntegerNumberSystem;AL|)
      (T (SETQ |IntegerNumberSystem;AL| (|IntegerNumberSystem;|)))))) 

(DEFUN |IntegerNumberSystem;| ()
  (PROG (#0=#:G1410)
    (RETURN
      (PROG1 (LETT #0#
                   (|sublisV|
                       (PAIR '(#1=#:G1404 #2=#:G1405 #3=#:G1406
                                  #4=#:G1407 #5=#:G1408 #6=#:G1409)
                             (LIST '(|Integer|) '(|Integer|)
                                   '(|Integer|) '(|InputForm|)
                                   '(|Pattern| (|Integer|))
                                   '(|Integer|)))
                       (|Join| (|UniqueFactorizationDomain|)
                               (|EuclideanDomain|)
                               (|OrderedIntegralDomain|)
                               (|DifferentialRing|)
                               (|ConvertibleTo| '#1#)
                               (|RetractableTo| '#2#)
                               (|LinearlyExplicitRingOver| '#3#)
                               (|ConvertibleTo| '#4#)
                               (|ConvertibleTo| '#5#)
                               (|PatternMatchable| '#6#)
                               (|CombinatorialFunctionCategory|)
                               (|RealConstant|) (|CharacteristicZero|)
                               (|StepThrough|)
                               (|mkCategory| '|domain|
                                   '(((|odd?| ((|Boolean|) $)) T)
                                     ((|even?| ((|Boolean|) $)) T)
                                     ((|base| ($)) T)
                                     ((|length| ($ $)) T)
                                     ((|shift| ($ $ $)) T)
                                     ((|bit?| ((|Boolean|) $ $)) T)
                                     ((|positiveRemainder| ($ $ $)) T)
                                     ((|symmetricRemainder| ($ $ $)) T)
                                     ((|rational?| ((|Boolean|) $)) T)
                                     ((|rational|
                                       ((|Fraction| (|Integer|)) $))
                                      T)
                                     ((|rationalIfCan|
                                       ((|Union|
                                         (|Fraction| (|Integer|))
                                         "failed")
                                        $))
                                      T)
                                     ((|random| ($)) T)
                                     ((|random| ($ $)) T)
                                     ((|copy| ($ $)) T)
                                     ((|inc| ($ $)) T)
                                     ((|dec| ($ $)) T)
                                     ((|mask| ($ $)) T)
                                     ((|addmod| ($ $ $ $)) T)
                                     ((|submod| ($ $ $ $)) T)
                                     ((|mulmod| ($ $ $ $)) T)
                                     ((|powmod| ($ $ $ $)) T)
                                     ((|invmod| ($ $ $)) T))
                                   '((|multiplicativeValuation| T)
                                     (|canonicalUnitNormal| T))
                                   '((|Fraction| (|Integer|))
                                     (|Boolean|))
                                   NIL)))
                   |IntegerNumberSystem|)
        (SETELT #0# 0 '(|IntegerNumberSystem|)))))) 

(SETQ |$CategoryFrame|
      (|put| '|IntegerNumberSystem| '|isCategory| T
             (|addModemap| '|IntegerNumberSystem|
                 '(|IntegerNumberSystem|) '((|Category|)) T
                 '|IntegerNumberSystem| |$CategoryFrame|))) 

(MAKEPROP '|IntegerNumberSystem| 'NILADIC T) 