
(/VERSIONCHECK 2) 

(DEFUN |PSETCAT-;elements| (|ps| $)
  (PROG (|lp|)
    (RETURN
      (LETT |lp| (SPADCALL |ps| (|getShellEntry| $ 12))
            |PSETCAT-;elements|)))) 

(DEFUN |PSETCAT-;variables1| (|lp| $)
  (PROG (#0=#:G1437 |p| #1=#:G1438 |lvars|)
    (RETURN
      (SEQ (LETT |lvars|
                 (PROGN
                   (LETT #0# NIL |PSETCAT-;variables1|)
                   (SEQ (LETT |p| NIL |PSETCAT-;variables1|)
                        (LETT #1# |lp| |PSETCAT-;variables1|) G190
                        (COND
                          ((OR (ATOM #1#)
                               (PROGN
                                 (LETT |p| (CAR #1#)
                                       |PSETCAT-;variables1|)
                                 NIL))
                           (GO G191)))
                        (SEQ (EXIT (LETT #0#
                                    (CONS
                                     (SPADCALL |p|
                                      (|getShellEntry| $ 14))
                                     #0#)
                                    |PSETCAT-;variables1|)))
                        (LETT #1# (CDR #1#) |PSETCAT-;variables1|)
                        (GO G190) G191 (EXIT (NREVERSE0 #0#))))
                 |PSETCAT-;variables1|)
           (EXIT (SPADCALL (CONS #'|PSETCAT-;variables1!0| $)
                     (SPADCALL
                         (SPADCALL |lvars| (|getShellEntry| $ 18))
                         (|getShellEntry| $ 19))
                     (|getShellEntry| $ 21))))))) 

(DEFUN |PSETCAT-;variables1!0| (|#1| |#2| $)
  (SPADCALL |#2| |#1| (|getShellEntry| $ 16))) 

(DEFUN |PSETCAT-;variables2| (|lp| $)
  (PROG (#0=#:G1442 |p| #1=#:G1443 |lvars|)
    (RETURN
      (SEQ (LETT |lvars|
                 (PROGN
                   (LETT #0# NIL |PSETCAT-;variables2|)
                   (SEQ (LETT |p| NIL |PSETCAT-;variables2|)
                        (LETT #1# |lp| |PSETCAT-;variables2|) G190
                        (COND
                          ((OR (ATOM #1#)
                               (PROGN
                                 (LETT |p| (CAR #1#)
                                       |PSETCAT-;variables2|)
                                 NIL))
                           (GO G191)))
                        (SEQ (EXIT (LETT #0#
                                    (CONS
                                     (SPADCALL |p|
                                      (|getShellEntry| $ 22))
                                     #0#)
                                    |PSETCAT-;variables2|)))
                        (LETT #1# (CDR #1#) |PSETCAT-;variables2|)
                        (GO G190) G191 (EXIT (NREVERSE0 #0#))))
                 |PSETCAT-;variables2|)
           (EXIT (SPADCALL (CONS #'|PSETCAT-;variables2!0| $)
                     (SPADCALL |lvars| (|getShellEntry| $ 19))
                     (|getShellEntry| $ 21))))))) 

(DEFUN |PSETCAT-;variables2!0| (|#1| |#2| $)
  (SPADCALL |#2| |#1| (|getShellEntry| $ 16))) 

(DEFUN |PSETCAT-;variables;SL;4| (|ps| $)
  (|PSETCAT-;variables1| (|PSETCAT-;elements| |ps| $) $)) 

(DEFUN |PSETCAT-;mainVariables;SL;5| (|ps| $)
  (|PSETCAT-;variables2|
      (SPADCALL (ELT $ 24) (|PSETCAT-;elements| |ps| $)
          (|getShellEntry| $ 26))
      $)) 

(DEFUN |PSETCAT-;mainVariable?;VarSetSB;6| (|v| |ps| $)
  (PROG (|lp|)
    (RETURN
      (SEQ (LETT |lp|
                 (SPADCALL (ELT $ 24) (|PSETCAT-;elements| |ps| $)
                     (|getShellEntry| $ 26))
                 |PSETCAT-;mainVariable?;VarSetSB;6|)
           (SEQ G190
                (COND
                  ((NULL (COND
                           ((NULL |lp|) 'NIL)
                           ('T
                            (SPADCALL
                                (SPADCALL
                                    (SPADCALL (|SPADfirst| |lp|)
                                     (|getShellEntry| $ 22))
                                    |v| (|getShellEntry| $ 28))
                                (|getShellEntry| $ 29)))))
                   (GO G191)))
                (SEQ (EXIT (LETT |lp| (CDR |lp|)
                                 |PSETCAT-;mainVariable?;VarSetSB;6|)))
                NIL (GO G190) G191 (EXIT NIL))
           (EXIT (SPADCALL (NULL |lp|) (|getShellEntry| $ 29))))))) 

(DEFUN |PSETCAT-;collectUnder;SVarSetS;7| (|ps| |v| $)
  (PROG (|p| |lp| |lq|)
    (RETURN
      (SEQ (LETT |lp| (|PSETCAT-;elements| |ps| $)
                 |PSETCAT-;collectUnder;SVarSetS;7|)
           (LETT |lq| NIL |PSETCAT-;collectUnder;SVarSetS;7|)
           (SEQ G190
                (COND
                  ((NULL (SPADCALL (NULL |lp|) (|getShellEntry| $ 29)))
                   (GO G191)))
                (SEQ (LETT |p| (|SPADfirst| |lp|)
                           |PSETCAT-;collectUnder;SVarSetS;7|)
                     (LETT |lp| (CDR |lp|)
                           |PSETCAT-;collectUnder;SVarSetS;7|)
                     (EXIT (COND
                             ((OR (SPADCALL |p| (|getShellEntry| $ 24))
                                  (SPADCALL
                                      (SPADCALL |p|
                                       (|getShellEntry| $ 22))
                                      |v| (|getShellEntry| $ 16)))
                              (LETT |lq| (CONS |p| |lq|)
                                    |PSETCAT-;collectUnder;SVarSetS;7|)))))
                NIL (GO G190) G191 (EXIT NIL))
           (EXIT (SPADCALL |lq| (|getShellEntry| $ 31))))))) 

(DEFUN |PSETCAT-;collectUpper;SVarSetS;8| (|ps| |v| $)
  (PROG (|p| |lp| |lq|)
    (RETURN
      (SEQ (LETT |lp| (|PSETCAT-;elements| |ps| $)
                 |PSETCAT-;collectUpper;SVarSetS;8|)
           (LETT |lq| NIL |PSETCAT-;collectUpper;SVarSetS;8|)
           (SEQ G190
                (COND
                  ((NULL (SPADCALL (NULL |lp|) (|getShellEntry| $ 29)))
                   (GO G191)))
                (SEQ (LETT |p| (|SPADfirst| |lp|)
                           |PSETCAT-;collectUpper;SVarSetS;8|)
                     (LETT |lp| (CDR |lp|)
                           |PSETCAT-;collectUpper;SVarSetS;8|)
                     (EXIT (COND
                             ((NULL (SPADCALL |p|
                                     (|getShellEntry| $ 24)))
                              (COND
                                ((SPADCALL |v|
                                     (SPADCALL |p|
                                      (|getShellEntry| $ 22))
                                     (|getShellEntry| $ 16))
                                 (LETT |lq| (CONS |p| |lq|)
                                       |PSETCAT-;collectUpper;SVarSetS;8|)))))))
                NIL (GO G190) G191 (EXIT NIL))
           (EXIT (SPADCALL |lq| (|getShellEntry| $ 31))))))) 

(DEFUN |PSETCAT-;collect;SVarSetS;9| (|ps| |v| $)
  (PROG (|p| |lp| |lq|)
    (RETURN
      (SEQ (LETT |lp| (|PSETCAT-;elements| |ps| $)
                 |PSETCAT-;collect;SVarSetS;9|)
           (LETT |lq| NIL |PSETCAT-;collect;SVarSetS;9|)
           (SEQ G190
                (COND
                  ((NULL (SPADCALL (NULL |lp|) (|getShellEntry| $ 29)))
                   (GO G191)))
                (SEQ (LETT |p| (|SPADfirst| |lp|)
                           |PSETCAT-;collect;SVarSetS;9|)
                     (LETT |lp| (CDR |lp|)
                           |PSETCAT-;collect;SVarSetS;9|)
                     (EXIT (COND
                             ((NULL (SPADCALL |p|
                                     (|getShellEntry| $ 24)))
                              (COND
                                ((SPADCALL
                                     (SPADCALL |p|
                                      (|getShellEntry| $ 22))
                                     |v| (|getShellEntry| $ 28))
                                 (LETT |lq| (CONS |p| |lq|)
                                       |PSETCAT-;collect;SVarSetS;9|)))))))
                NIL (GO G190) G191 (EXIT NIL))
           (EXIT (SPADCALL |lq| (|getShellEntry| $ 31))))))) 

(DEFUN |PSETCAT-;sort;SVarSetR;10| (|ps| |v| $)
  (PROG (|p| |lp| |us| |vs| |ws|)
    (RETURN
      (SEQ (LETT |lp| (|PSETCAT-;elements| |ps| $)
                 |PSETCAT-;sort;SVarSetR;10|)
           (LETT |us| NIL |PSETCAT-;sort;SVarSetR;10|)
           (LETT |vs| NIL |PSETCAT-;sort;SVarSetR;10|)
           (LETT |ws| NIL |PSETCAT-;sort;SVarSetR;10|)
           (SEQ G190
                (COND
                  ((NULL (SPADCALL (NULL |lp|) (|getShellEntry| $ 29)))
                   (GO G191)))
                (SEQ (LETT |p| (|SPADfirst| |lp|)
                           |PSETCAT-;sort;SVarSetR;10|)
                     (LETT |lp| (CDR |lp|) |PSETCAT-;sort;SVarSetR;10|)
                     (EXIT (COND
                             ((OR (SPADCALL |p| (|getShellEntry| $ 24))
                                  (SPADCALL
                                      (SPADCALL |p|
                                       (|getShellEntry| $ 22))
                                      |v| (|getShellEntry| $ 16)))
                              (LETT |us| (CONS |p| |us|)
                                    |PSETCAT-;sort;SVarSetR;10|))
                             ((SPADCALL
                                  (SPADCALL |p| (|getShellEntry| $ 22))
                                  |v| (|getShellEntry| $ 28))
                              (LETT |vs| (CONS |p| |vs|)
                                    |PSETCAT-;sort;SVarSetR;10|))
                             ('T
                              (LETT |ws| (CONS |p| |ws|)
                                    |PSETCAT-;sort;SVarSetR;10|)))))
                NIL (GO G190) G191 (EXIT NIL))
           (EXIT (VECTOR (SPADCALL |us| (|getShellEntry| $ 31))
                         (SPADCALL |vs| (|getShellEntry| $ 31))
                         (SPADCALL |ws| (|getShellEntry| $ 31)))))))) 

(DEFUN |PSETCAT-;=;2SB;11| (|ps1| |ps2| $)
  (PROG (#0=#:G1477 #1=#:G1478 #2=#:G1479 |p| #3=#:G1480)
    (RETURN
      (SEQ (SPADCALL
               (SPADCALL
                   (PROGN
                     (LETT #0# NIL |PSETCAT-;=;2SB;11|)
                     (SEQ (LETT |p| NIL |PSETCAT-;=;2SB;11|)
                          (LETT #1# (|PSETCAT-;elements| |ps1| $)
                                |PSETCAT-;=;2SB;11|)
                          G190
                          (COND
                            ((OR (ATOM #1#)
                                 (PROGN
                                   (LETT |p| (CAR #1#)
                                    |PSETCAT-;=;2SB;11|)
                                   NIL))
                             (GO G191)))
                          (SEQ (EXIT (LETT #0# (CONS |p| #0#)
                                      |PSETCAT-;=;2SB;11|)))
                          (LETT #1# (CDR #1#) |PSETCAT-;=;2SB;11|)
                          (GO G190) G191 (EXIT (NREVERSE0 #0#))))
                   (|getShellEntry| $ 38))
               (SPADCALL
                   (PROGN
                     (LETT #2# NIL |PSETCAT-;=;2SB;11|)
                     (SEQ (LETT |p| NIL |PSETCAT-;=;2SB;11|)
                          (LETT #3# (|PSETCAT-;elements| |ps2| $)
                                |PSETCAT-;=;2SB;11|)
                          G190
                          (COND
                            ((OR (ATOM #3#)
                                 (PROGN
                                   (LETT |p| (CAR #3#)
                                    |PSETCAT-;=;2SB;11|)
                                   NIL))
                             (GO G191)))
                          (SEQ (EXIT (LETT #2# (CONS |p| #2#)
                                      |PSETCAT-;=;2SB;11|)))
                          (LETT #3# (CDR #3#) |PSETCAT-;=;2SB;11|)
                          (GO G190) G191 (EXIT (NREVERSE0 #2#))))
                   (|getShellEntry| $ 38))
               (|getShellEntry| $ 39)))))) 

(DEFUN |PSETCAT-;localInf?| (|p| |q| $)
  (SPADCALL (SPADCALL |p| (|getShellEntry| $ 41))
      (SPADCALL |q| (|getShellEntry| $ 41)) (|getShellEntry| $ 42))) 

(DEFUN |PSETCAT-;localTriangular?| (|lp| $)
  (PROG (|q| |p|)
    (RETURN
      (SEQ (LETT |lp| (SPADCALL (ELT $ 43) |lp| (|getShellEntry| $ 26))
                 |PSETCAT-;localTriangular?|)
           (EXIT (COND
                   ((NULL |lp|) 'T)
                   ((SPADCALL (ELT $ 24) |lp| (|getShellEntry| $ 44))
                    'NIL)
                   ('T
                    (SEQ (LETT |lp|
                               (SPADCALL
                                   (CONS
                                    #'|PSETCAT-;localTriangular?!0| $)
                                   |lp| (|getShellEntry| $ 46))
                               |PSETCAT-;localTriangular?|)
                         (LETT |p| (|SPADfirst| |lp|)
                               |PSETCAT-;localTriangular?|)
                         (LETT |lp| (CDR |lp|)
                               |PSETCAT-;localTriangular?|)
                         (SEQ G190
                              (COND
                                ((NULL (COND
                                         ((NULL |lp|) 'NIL)
                                         ('T
                                          (SPADCALL
                                           (SPADCALL
                                            (LETT |q|
                                             (|SPADfirst| |lp|)
                                             |PSETCAT-;localTriangular?|)
                                            (|getShellEntry| $ 22))
                                           (SPADCALL |p|
                                            (|getShellEntry| $ 22))
                                           (|getShellEntry| $ 16)))))
                                 (GO G191)))
                              (SEQ (LETT |p| |q|
                                    |PSETCAT-;localTriangular?|)
                                   (EXIT
                                    (LETT |lp| (CDR |lp|)
                                     |PSETCAT-;localTriangular?|)))
                              NIL (GO G190) G191 (EXIT NIL))
                         (EXIT (NULL |lp|)))))))))) 

(DEFUN |PSETCAT-;localTriangular?!0| (|#1| |#2| $)
  (SPADCALL (SPADCALL |#2| (|getShellEntry| $ 22))
      (SPADCALL |#1| (|getShellEntry| $ 22)) (|getShellEntry| $ 16))) 

(DEFUN |PSETCAT-;triangular?;SB;14| (|ps| $)
  (|PSETCAT-;localTriangular?| (|PSETCAT-;elements| |ps| $) $)) 

(DEFUN |PSETCAT-;trivialIdeal?;SB;15| (|ps| $)
  (NULL (SPADCALL (ELT $ 43) (|PSETCAT-;elements| |ps| $)
            (|getShellEntry| $ 26)))) 

(DEFUN |PSETCAT-;roughUnitIdeal?;SB;16| (|ps| $)
  (SPADCALL (ELT $ 24)
      (SPADCALL (ELT $ 43) (|PSETCAT-;elements| |ps| $)
          (|getShellEntry| $ 26))
      (|getShellEntry| $ 44))) 

(DEFUN |PSETCAT-;relativelyPrimeLeadingMonomials?| (|p| |q| $)
  (PROG (|dp| |dq|)
    (RETURN
      (SEQ (LETT |dp| (SPADCALL |p| (|getShellEntry| $ 41))
                 |PSETCAT-;relativelyPrimeLeadingMonomials?|)
           (LETT |dq| (SPADCALL |q| (|getShellEntry| $ 41))
                 |PSETCAT-;relativelyPrimeLeadingMonomials?|)
           (EXIT (SPADCALL (SPADCALL |dp| |dq| (|getShellEntry| $ 50))
                     (SPADCALL |dp| |dq| (|getShellEntry| $ 51))
                     (|getShellEntry| $ 52))))))) 

(DEFUN |PSETCAT-;roughBase?;SB;18| (|ps| $)
  (PROG (|p| |lp| |rB?| |copylp|)
    (RETURN
      (SEQ (LETT |lp|
                 (SPADCALL (ELT $ 43) (|PSETCAT-;elements| |ps| $)
                     (|getShellEntry| $ 26))
                 |PSETCAT-;roughBase?;SB;18|)
           (EXIT (COND
                   ((NULL |lp|) 'T)
                   ('T
                    (SEQ (LETT |rB?| 'T |PSETCAT-;roughBase?;SB;18|)
                         (SEQ G190
                              (COND
                                ((NULL (COND
                                         ((NULL |lp|) 'NIL)
                                         ('T |rB?|)))
                                 (GO G191)))
                              (SEQ (LETT |p| (|SPADfirst| |lp|)
                                    |PSETCAT-;roughBase?;SB;18|)
                                   (LETT |lp| (CDR |lp|)
                                    |PSETCAT-;roughBase?;SB;18|)
                                   (LETT |copylp| |lp|
                                    |PSETCAT-;roughBase?;SB;18|)
                                   (EXIT
                                    (SEQ G190
                                     (COND
                                       ((NULL
                                         (COND
                                           ((NULL |copylp|) 'NIL)
                                           ('T |rB?|)))
                                        (GO G191)))
                                     (SEQ
                                      (LETT |rB?|
                                       (|PSETCAT-;relativelyPrimeLeadingMonomials?|
                                        |p| (|SPADfirst| |copylp|) $)
                                       |PSETCAT-;roughBase?;SB;18|)
                                      (EXIT
                                       (LETT |copylp| (CDR |copylp|)
                                        |PSETCAT-;roughBase?;SB;18|)))
                                     NIL (GO G190) G191 (EXIT NIL))))
                              NIL (GO G190) G191 (EXIT NIL))
                         (EXIT |rB?|))))))))) 

(DEFUN |PSETCAT-;roughSubIdeal?;2SB;19| (|ps1| |ps2| $)
  (PROG (|lp|)
    (RETURN
      (SEQ (LETT |lp|
                 (SPADCALL (|PSETCAT-;elements| |ps1| $) |ps2|
                     (|getShellEntry| $ 54))
                 |PSETCAT-;roughSubIdeal?;2SB;19|)
           (EXIT (NULL (SPADCALL (ELT $ 43) |lp|
                           (|getShellEntry| $ 26)))))))) 

(DEFUN |PSETCAT-;roughEqualIdeals?;2SB;20| (|ps1| |ps2| $)
  (COND
    ((SPADCALL |ps1| |ps2| (|getShellEntry| $ 56)) 'T)
    ((SPADCALL |ps1| |ps2| (|getShellEntry| $ 57))
     (SPADCALL |ps2| |ps1| (|getShellEntry| $ 57)))
    ('T 'NIL))) 

(DEFUN |PSETCAT-;exactQuo| (|r| |s| $)
  (PROG (#0=#:G1512)
    (RETURN
      (COND
        ((|HasCategory| (|getShellEntry| $ 7) '(|EuclideanDomain|))
         (SPADCALL |r| |s| (|getShellEntry| $ 59)))
        ('T
         (PROG2 (LETT #0# (SPADCALL |r| |s| (|getShellEntry| $ 61))
                      |PSETCAT-;exactQuo|)
                (QCDR #0#)
           (|check-union| (QEQCAR #0# 0) (|getShellEntry| $ 7) #0#))))))) 

(DEFUN |PSETCAT-;headRemainder;PSR;22| (|a| |ps| $)
  (PROG (|lp1| |p| |e| |g| |#G45| |#G46| |lca| |lcp| |r| |lp2|)
    (RETURN
      (SEQ (LETT |lp1|
                 (SPADCALL (ELT $ 43) (|PSETCAT-;elements| |ps| $)
                     (|getShellEntry| $ 26))
                 |PSETCAT-;headRemainder;PSR;22|)
           (EXIT (COND
                   ((NULL |lp1|) (CONS |a| (|spadConstant| $ 62)))
                   ((SPADCALL (ELT $ 24) |lp1| (|getShellEntry| $ 44))
                    (CONS (SPADCALL |a| (|getShellEntry| $ 63))
                          (|spadConstant| $ 62)))
                   ('T
                    (SEQ (LETT |r| (|spadConstant| $ 62)
                               |PSETCAT-;headRemainder;PSR;22|)
                         (LETT |lp1|
                               (SPADCALL
                                   (CONS
                                    (|function| |PSETCAT-;localInf?|)
                                    $)
                                   (REVERSE
                                    (|PSETCAT-;elements| |ps| $))
                                   (|getShellEntry| $ 46))
                               |PSETCAT-;headRemainder;PSR;22|)
                         (LETT |lp2| |lp1|
                               |PSETCAT-;headRemainder;PSR;22|)
                         (SEQ G190
                              (COND
                                ((NULL (COND
                                         ((SPADCALL |a|
                                           (|getShellEntry| $ 43))
                                          'NIL)
                                         ('T
                                          (SPADCALL (NULL |lp2|)
                                           (|getShellEntry| $ 29)))))
                                 (GO G191)))
                              (SEQ (LETT |p| (|SPADfirst| |lp2|)
                                    |PSETCAT-;headRemainder;PSR;22|)
                                   (LETT |e|
                                    (SPADCALL
                                     (SPADCALL |a|
                                      (|getShellEntry| $ 41))
                                     (SPADCALL |p|
                                      (|getShellEntry| $ 41))
                                     (|getShellEntry| $ 64))
                                    |PSETCAT-;headRemainder;PSR;22|)
                                   (EXIT
                                    (COND
                                      ((QEQCAR |e| 0)
                                       (SEQ
                                        (LETT |g|
                                         (SPADCALL
                                          (LETT |lca|
                                           (SPADCALL |a|
                                            (|getShellEntry| $ 65))
                                           |PSETCAT-;headRemainder;PSR;22|)
                                          (LETT |lcp|
                                           (SPADCALL |p|
                                            (|getShellEntry| $ 65))
                                           |PSETCAT-;headRemainder;PSR;22|)
                                          (|getShellEntry| $ 66))
                                         |PSETCAT-;headRemainder;PSR;22|)
                                        (PROGN
                                          (LETT |#G45|
                                           (|PSETCAT-;exactQuo| |lca|
                                            |g| $)
                                           |PSETCAT-;headRemainder;PSR;22|)
                                          (LETT |#G46|
                                           (|PSETCAT-;exactQuo| |lcp|
                                            |g| $)
                                           |PSETCAT-;headRemainder;PSR;22|)
                                          (LETT |lca| |#G45|
                                           |PSETCAT-;headRemainder;PSR;22|)
                                          (LETT |lcp| |#G46|
                                           |PSETCAT-;headRemainder;PSR;22|))
                                        (LETT |a|
                                         (SPADCALL
                                          (SPADCALL |lcp|
                                           (SPADCALL |a|
                                            (|getShellEntry| $ 63))
                                           (|getShellEntry| $ 67))
                                          (SPADCALL
                                           (SPADCALL |lca| (QCDR |e|)
                                            (|getShellEntry| $ 68))
                                           (SPADCALL |p|
                                            (|getShellEntry| $ 63))
                                           (|getShellEntry| $ 69))
                                          (|getShellEntry| $ 70))
                                         |PSETCAT-;headRemainder;PSR;22|)
                                        (LETT |r|
                                         (SPADCALL |r| |lcp|
                                          (|getShellEntry| $ 71))
                                         |PSETCAT-;headRemainder;PSR;22|)
                                        (EXIT
                                         (LETT |lp2| |lp1|
                                          |PSETCAT-;headRemainder;PSR;22|))))
                                      ('T
                                       (LETT |lp2| (CDR |lp2|)
                                        |PSETCAT-;headRemainder;PSR;22|)))))
                              NIL (GO G190) G191 (EXIT NIL))
                         (EXIT (CONS |a| |r|)))))))))) 

(DEFUN |PSETCAT-;makeIrreducible!| (|frac| $)
  (PROG (|g|)
    (RETURN
      (SEQ (LETT |g|
                 (SPADCALL (QCDR |frac|) (QCAR |frac|)
                     (|getShellEntry| $ 74))
                 |PSETCAT-;makeIrreducible!|)
           (EXIT (COND
                   ((SPADCALL |g| (|spadConstant| $ 62)
                        (|getShellEntry| $ 76))
                    |frac|)
                   ('T
                    (SEQ (PROGN
                           (RPLACA |frac|
                                   (SPADCALL (QCAR |frac|) |g|
                                    (|getShellEntry| $ 77)))
                           (QCAR |frac|))
                         (PROGN
                           (RPLACD |frac|
                                   (|PSETCAT-;exactQuo| (QCDR |frac|)
                                    |g| $))
                           (QCDR |frac|))
                         (EXIT |frac|))))))))) 

(DEFUN |PSETCAT-;remainder;PSR;24| (|a| |ps| $)
  (PROG (|hRa| |r| |lca| |g| |b| |c|)
    (RETURN
      (SEQ (LETT |hRa|
                 (|PSETCAT-;makeIrreducible!|
                     (SPADCALL |a| |ps| (|getShellEntry| $ 78)) $)
                 |PSETCAT-;remainder;PSR;24|)
           (LETT |a| (QCAR |hRa|) |PSETCAT-;remainder;PSR;24|)
           (LETT |r| (QCDR |hRa|) |PSETCAT-;remainder;PSR;24|)
           (EXIT (COND
                   ((SPADCALL |a| (|getShellEntry| $ 43))
                    (VECTOR (|spadConstant| $ 62) |a| |r|))
                   ('T
                    (SEQ (LETT |b|
                               (SPADCALL (|spadConstant| $ 62)
                                   (SPADCALL |a|
                                    (|getShellEntry| $ 41))
                                   (|getShellEntry| $ 68))
                               |PSETCAT-;remainder;PSR;24|)
                         (LETT |c|
                               (SPADCALL |a| (|getShellEntry| $ 65))
                               |PSETCAT-;remainder;PSR;24|)
                         (SEQ G190
                              (COND
                                ((NULL (SPADCALL
                                        (SPADCALL
                                         (LETT |a|
                                          (SPADCALL |a|
                                           (|getShellEntry| $ 63))
                                          |PSETCAT-;remainder;PSR;24|)
                                         (|getShellEntry| $ 43))
                                        (|getShellEntry| $ 29)))
                                 (GO G191)))
                              (SEQ (LETT |hRa|
                                    (|PSETCAT-;makeIrreducible!|
                                     (SPADCALL |a| |ps|
                                      (|getShellEntry| $ 78))
                                     $)
                                    |PSETCAT-;remainder;PSR;24|)
                                   (LETT |a| (QCAR |hRa|)
                                    |PSETCAT-;remainder;PSR;24|)
                                   (LETT |r|
                                    (SPADCALL |r| (QCDR |hRa|)
                                     (|getShellEntry| $ 71))
                                    |PSETCAT-;remainder;PSR;24|)
                                   (LETT |g|
                                    (SPADCALL |c|
                                     (LETT |lca|
                                      (SPADCALL |a|
                                       (|getShellEntry| $ 65))
                                      |PSETCAT-;remainder;PSR;24|)
                                     (|getShellEntry| $ 66))
                                    |PSETCAT-;remainder;PSR;24|)
                                   (LETT |b|
                                    (SPADCALL
                                     (SPADCALL
                                      (SPADCALL (QCDR |hRa|)
                                       (|PSETCAT-;exactQuo| |c| |g| $)
                                       (|getShellEntry| $ 71))
                                      |b| (|getShellEntry| $ 67))
                                     (SPADCALL
                                      (|PSETCAT-;exactQuo| |lca| |g| $)
                                      (SPADCALL |a|
                                       (|getShellEntry| $ 41))
                                      (|getShellEntry| $ 68))
                                     (|getShellEntry| $ 79))
                                    |PSETCAT-;remainder;PSR;24|)
                                   (EXIT
                                    (LETT |c| |g|
                                     |PSETCAT-;remainder;PSR;24|)))
                              NIL (GO G190) G191 (EXIT NIL))
                         (EXIT (VECTOR |c| |b| |r|)))))))))) 

(DEFUN |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25| (|ps| |cs| $)
  (PROG (|p| |rs|)
    (RETURN
      (SEQ (COND
             ((SPADCALL |cs| (|getShellEntry| $ 82)) |ps|)
             ((SPADCALL |cs| (|getShellEntry| $ 83))
              (LIST (|spadConstant| $ 84)))
             ('T
              (SEQ (LETT |ps|
                         (SPADCALL (ELT $ 43) |ps|
                             (|getShellEntry| $ 26))
                         |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                   (EXIT (COND
                           ((NULL |ps|) |ps|)
                           ((SPADCALL (ELT $ 24) |ps|
                                (|getShellEntry| $ 44))
                            (LIST (|spadConstant| $ 75)))
                           ('T
                            (SEQ (LETT |rs| NIL
                                       |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                                 (SEQ G190
                                      (COND
                                        ((NULL
                                          (SPADCALL (NULL |ps|)
                                           (|getShellEntry| $ 29)))
                                         (GO G191)))
                                      (SEQ
                                       (LETT |p| (|SPADfirst| |ps|)
                                        |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                                       (LETT |ps| (CDR |ps|)
                                        |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                                       (LETT |p|
                                        (QCAR
                                         (SPADCALL |p| |cs|
                                          (|getShellEntry| $ 78)))
                                        |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                                       (EXIT
                                        (COND
                                          ((NULL
                                            (SPADCALL |p|
                                             (|getShellEntry| $ 43)))
                                           (COND
                                             ((SPADCALL |p|
                                               (|getShellEntry| $ 24))
                                              (SEQ
                                               (LETT |ps| NIL
                                                |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                                               (EXIT
                                                (LETT |rs|
                                                 (LIST
                                                  (|spadConstant| $ 75))
                                                 |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|))))
                                             ('T
                                              (SEQ
                                               (SPADCALL |p|
                                                (|getShellEntry| $ 85))
                                               (EXIT
                                                (LETT |rs|
                                                 (CONS |p| |rs|)
                                                 |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)))))))))
                                      NIL (GO G190) G191 (EXIT NIL))
                                 (EXIT (SPADCALL |rs|
                                        (|getShellEntry| $ 86)))))))))))))) 

(DEFUN |PSETCAT-;rewriteIdealWithRemainder;LSL;26| (|ps| |cs| $)
  (PROG (|p| |rs|)
    (RETURN
      (SEQ (COND
             ((SPADCALL |cs| (|getShellEntry| $ 82)) |ps|)
             ((SPADCALL |cs| (|getShellEntry| $ 83))
              (LIST (|spadConstant| $ 84)))
             ('T
              (SEQ (LETT |ps|
                         (SPADCALL (ELT $ 43) |ps|
                             (|getShellEntry| $ 26))
                         |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                   (EXIT (COND
                           ((NULL |ps|) |ps|)
                           ((SPADCALL (ELT $ 24) |ps|
                                (|getShellEntry| $ 44))
                            (LIST (|spadConstant| $ 75)))
                           ('T
                            (SEQ (LETT |rs| NIL
                                       |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                                 (SEQ G190
                                      (COND
                                        ((NULL
                                          (SPADCALL (NULL |ps|)
                                           (|getShellEntry| $ 29)))
                                         (GO G191)))
                                      (SEQ
                                       (LETT |p| (|SPADfirst| |ps|)
                                        |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                                       (LETT |ps| (CDR |ps|)
                                        |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                                       (LETT |p|
                                        (QVELT
                                         (SPADCALL |p| |cs|
                                          (|getShellEntry| $ 88))
                                         1)
                                        |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                                       (EXIT
                                        (COND
                                          ((NULL
                                            (SPADCALL |p|
                                             (|getShellEntry| $ 43)))
                                           (COND
                                             ((SPADCALL |p|
                                               (|getShellEntry| $ 24))
                                              (SEQ
                                               (LETT |ps| NIL
                                                |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                                               (EXIT
                                                (LETT |rs|
                                                 (LIST
                                                  (|spadConstant| $ 75))
                                                 |PSETCAT-;rewriteIdealWithRemainder;LSL;26|))))
                                             ('T
                                              (LETT |rs|
                                               (CONS
                                                (SPADCALL |p|
                                                 (|getShellEntry| $ 89))
                                                |rs|)
                                               |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)))))))
                                      NIL (GO G190) G191 (EXIT NIL))
                                 (EXIT (SPADCALL |rs|
                                        (|getShellEntry| $ 86)))))))))))))) 

(DEFUN |PolynomialSetCategory&| (|#1| |#2| |#3| |#4| |#5|)
  (PROG (|dv$1| |dv$2| |dv$3| |dv$4| |dv$5| |dv$| $ |pv$|)
    (RETURN
      (PROGN
        (LETT |dv$1| (|devaluate| |#1|)
              . #0=(|PolynomialSetCategory&|))
        (LETT |dv$2| (|devaluate| |#2|) . #0#)
        (LETT |dv$3| (|devaluate| |#3|) . #0#)
        (LETT |dv$4| (|devaluate| |#4|) . #0#)
        (LETT |dv$5| (|devaluate| |#5|) . #0#)
        (LETT |dv$|
              (LIST '|PolynomialSetCategory&| |dv$1| |dv$2| |dv$3|
                    |dv$4| |dv$5|) . #0#)
        (LETT $ (|newShell| 91) . #0#)
        (|setShellEntry| $ 0 |dv$|)
        (|setShellEntry| $ 3
            (LETT |pv$|
                  (|buildPredVector| 0 0
                      (LIST (|HasCategory| |#2| '(|IntegralDomain|)))) . #0#))
        (|stuffDomainSlots| $)
        (|setShellEntry| $ 6 |#1|)
        (|setShellEntry| $ 7 |#2|)
        (|setShellEntry| $ 8 |#3|)
        (|setShellEntry| $ 9 |#4|)
        (|setShellEntry| $ 10 |#5|)
        (COND
          ((|testBitVector| |pv$| 1)
           (PROGN
             (|setShellEntry| $ 49
                 (CONS (|dispatchFunction|
                           |PSETCAT-;roughUnitIdeal?;SB;16|)
                       $))
             (|setShellEntry| $ 53
                 (CONS (|dispatchFunction| |PSETCAT-;roughBase?;SB;18|)
                       $))
             (|setShellEntry| $ 55
                 (CONS (|dispatchFunction|
                           |PSETCAT-;roughSubIdeal?;2SB;19|)
                       $))
             (|setShellEntry| $ 58
                 (CONS (|dispatchFunction|
                           |PSETCAT-;roughEqualIdeals?;2SB;20|)
                       $)))))
        (COND
          ((|HasCategory| |#2| '(|GcdDomain|))
           (COND
             ((|HasCategory| |#4| '(|ConvertibleTo| (|Symbol|)))
              (PROGN
                (|setShellEntry| $ 73
                    (CONS (|dispatchFunction|
                              |PSETCAT-;headRemainder;PSR;22|)
                          $))
                (|setShellEntry| $ 81
                    (CONS (|dispatchFunction|
                              |PSETCAT-;remainder;PSR;24|)
                          $))
                (|setShellEntry| $ 87
                    (CONS (|dispatchFunction|
                              |PSETCAT-;rewriteIdealWithHeadRemainder;LSL;25|)
                          $))
                (|setShellEntry| $ 90
                    (CONS (|dispatchFunction|
                              |PSETCAT-;rewriteIdealWithRemainder;LSL;26|)
                          $)))))))
        $)))) 

(MAKEPROP '|PolynomialSetCategory&| '|infovec|
    (LIST '#(NIL NIL NIL NIL NIL NIL (|local| |#1|) (|local| |#2|)
             (|local| |#3|) (|local| |#4|) (|local| |#5|) (|List| 10)
             (0 . |members|) (|List| 9) (5 . |variables|) (|Boolean|)
             (10 . <) (|List| $) (16 . |concat|)
             (21 . |removeDuplicates|) (|Mapping| 15 9 9) (26 . |sort|)
             (32 . |mvar|) |PSETCAT-;variables;SL;4| (37 . |ground?|)
             (|Mapping| 15 10) (42 . |remove|)
             |PSETCAT-;mainVariables;SL;5| (48 . =) (54 . |not|)
             |PSETCAT-;mainVariable?;VarSetSB;6| (59 . |construct|)
             |PSETCAT-;collectUnder;SVarSetS;7|
             |PSETCAT-;collectUpper;SVarSetS;8|
             |PSETCAT-;collect;SVarSetS;9|
             (|Record| (|:| |under| $) (|:| |floor| $) (|:| |upper| $))
             |PSETCAT-;sort;SVarSetR;10| (|Set| 10) (64 . |brace|)
             (69 . =) |PSETCAT-;=;2SB;11| (75 . |degree|) (80 . <)
             (86 . |zero?|) (91 . |any?|) (|Mapping| 15 10 10)
             (97 . |sort|) |PSETCAT-;triangular?;SB;14|
             |PSETCAT-;trivialIdeal?;SB;15| (103 . |roughUnitIdeal?|)
             (108 . |sup|) (114 . +) (120 . =) (126 . |roughBase?|)
             (131 . |rewriteIdealWithRemainder|)
             (137 . |roughSubIdeal?|) (143 . =)
             (149 . |roughSubIdeal?|) (155 . |roughEqualIdeals?|)
             (161 . |quo|) (|Union| $ '"failed") (167 . |exquo|)
             (173 . |One|) (177 . |reductum|) (182 . |subtractIfCan|)
             (188 . |leadingCoefficient|) (193 . |gcd|) (199 . *)
             (205 . |monomial|) (211 . *) (217 . -) (223 . *)
             (|Record| (|:| |num| 10) (|:| |den| 7))
             (229 . |headRemainder|) (235 . |gcd|) (241 . |One|)
             (245 . =) (251 . |exactQuotient!|) (257 . |headRemainder|)
             (263 . +)
             (|Record| (|:| |rnum| 7) (|:| |polnum| 10) (|:| |den| 7))
             (269 . |remainder|) (275 . |trivialIdeal?|)
             (280 . |roughUnitIdeal?|) (285 . |Zero|)
             (289 . |primitivePart!|) (294 . |removeDuplicates|)
             (299 . |rewriteIdealWithHeadRemainder|)
             (305 . |remainder|) (311 . |unitCanonical|)
             (316 . |rewriteIdealWithRemainder|))
          '#(|variables| 322 |trivialIdeal?| 327 |triangular?| 332
             |sort| 337 |roughUnitIdeal?| 343 |roughSubIdeal?| 348
             |roughEqualIdeals?| 354 |roughBase?| 360
             |rewriteIdealWithRemainder| 365
             |rewriteIdealWithHeadRemainder| 371 |remainder| 377
             |mainVariables| 383 |mainVariable?| 388 |headRemainder|
             394 |collectUpper| 400 |collectUnder| 406 |collect| 412 =
             418)
          'NIL
          (CONS (|makeByteWordVec2| 1 'NIL)
                (CONS '#()
                      (CONS '#()
                            (|makeByteWordVec2| 90
                                '(1 6 11 0 12 1 10 13 0 14 2 9 15 0 0
                                  16 1 13 0 17 18 1 13 0 0 19 2 13 0 20
                                  0 21 1 10 9 0 22 1 10 15 0 24 2 11 0
                                  25 0 26 2 9 15 0 0 28 1 15 0 0 29 1 6
                                  0 11 31 1 37 0 11 38 2 37 15 0 0 39 1
                                  10 8 0 41 2 8 15 0 0 42 1 10 15 0 43
                                  2 11 15 25 0 44 2 11 0 45 0 46 1 0 15
                                  0 49 2 8 0 0 0 50 2 8 0 0 0 51 2 8 15
                                  0 0 52 1 0 15 0 53 2 6 11 11 0 54 2 0
                                  15 0 0 55 2 6 15 0 0 56 2 6 15 0 0 57
                                  2 0 15 0 0 58 2 7 0 0 0 59 2 7 60 0 0
                                  61 0 7 0 62 1 10 0 0 63 2 8 60 0 0 64
                                  1 10 7 0 65 2 7 0 0 0 66 2 10 0 7 0
                                  67 2 10 0 7 8 68 2 10 0 0 0 69 2 10 0
                                  0 0 70 2 7 0 0 0 71 2 0 72 10 0 73 2
                                  10 7 7 0 74 0 10 0 75 2 7 15 0 0 76 2
                                  10 0 0 7 77 2 6 72 10 0 78 2 10 0 0 0
                                  79 2 0 80 10 0 81 1 6 15 0 82 1 6 15
                                  0 83 0 10 0 84 1 10 0 0 85 1 11 0 0
                                  86 2 0 11 11 0 87 2 6 80 10 0 88 1 10
                                  0 0 89 2 0 11 11 0 90 1 0 13 0 23 1 0
                                  15 0 48 1 0 15 0 47 2 0 35 0 9 36 1 0
                                  15 0 49 2 0 15 0 0 55 2 0 15 0 0 58 1
                                  0 15 0 53 2 0 11 11 0 90 2 0 11 11 0
                                  87 2 0 80 10 0 81 1 0 13 0 27 2 0 15
                                  9 0 30 2 0 72 10 0 73 2 0 0 0 9 33 2
                                  0 0 0 9 32 2 0 0 0 9 34 2 0 15 0 0
                                  40)))))
          '|lookupComplete|)) 

(SETQ |$CategoryFrame|
      (|put| '|PolynomialSetCategory&| '|isFunctor|
             '(((|triangular?| ((|Boolean|) $)) T (ELT $ 47))
               ((|rewriteIdealWithRemainder|
                    ((|List| |#5|) (|List| |#5|) $))
                T (ELT $ 90))
               ((|rewriteIdealWithHeadRemainder|
                    ((|List| |#5|) (|List| |#5|) $))
                T (ELT $ 87))
               ((|remainder|
                    ((|Record| (|:| |rnum| |#2|) (|:| |polnum| |#5|)
                         (|:| |den| |#2|))
                     |#5| $))
                T (ELT $ 81))
               ((|headRemainder|
                    ((|Record| (|:| |num| |#5|) (|:| |den| |#2|)) |#5|
                     $))
                T (ELT $ 73))
               ((|roughUnitIdeal?| ((|Boolean|) $)) T (ELT $ 49))
               ((|roughEqualIdeals?| ((|Boolean|) $ $)) T (ELT $ 58))
               ((|roughSubIdeal?| ((|Boolean|) $ $)) T (ELT $ 55))
               ((|roughBase?| ((|Boolean|) $)) T (ELT $ 53))
               ((|trivialIdeal?| ((|Boolean|) $)) T (ELT $ 48))
               ((|sort| ((|Record| (|:| |under| $) (|:| |floor| $)
                             (|:| |upper| $))
                         $ |#4|))
                T (ELT $ 36))
               ((|collectUpper| ($ $ |#4|)) T (ELT $ 33))
               ((|collect| ($ $ |#4|)) T (ELT $ 34))
               ((|collectUnder| ($ $ |#4|)) T (ELT $ 32))
               ((|mainVariable?| ((|Boolean|) |#4| $)) T (ELT $ 30))
               ((|mainVariables| ((|List| |#4|) $)) T (ELT $ 27))
               ((|variables| ((|List| |#4|) $)) T (ELT $ 23))
               ((= ((|Boolean|) $ $)) T (ELT $ 40)))
             (|addModemap| '|PolynomialSetCategory&|
                 '(|PolynomialSetCategory&| |#1| |#2| |#3| |#4| |#5|)
                 '((CATEGORY |domain|
                       (SIGNATURE |triangular?| ((|Boolean|) |#1|))
                       (SIGNATURE |rewriteIdealWithRemainder|
                           ((|List| |#5|) (|List| |#5|) |#1|))
                       (SIGNATURE |rewriteIdealWithHeadRemainder|
                           ((|List| |#5|) (|List| |#5|) |#1|))
                       (SIGNATURE |remainder|
                           ((|Record| (|:| |rnum| |#2|)
                                (|:| |polnum| |#5|) (|:| |den| |#2|))
                            |#5| |#1|))
                       (SIGNATURE |headRemainder|
                           ((|Record| (|:| |num| |#5|)
                                (|:| |den| |#2|))
                            |#5| |#1|))
                       (SIGNATURE |roughUnitIdeal?| ((|Boolean|) |#1|))
                       (SIGNATURE |roughEqualIdeals?|
                           ((|Boolean|) |#1| |#1|))
                       (SIGNATURE |roughSubIdeal?|
                           ((|Boolean|) |#1| |#1|))
                       (SIGNATURE |roughBase?| ((|Boolean|) |#1|))
                       (SIGNATURE |trivialIdeal?| ((|Boolean|) |#1|))
                       (SIGNATURE |sort|
                           ((|Record| (|:| |under| |#1|)
                                (|:| |floor| |#1|) (|:| |upper| |#1|))
                            |#1| |#4|))
                       (SIGNATURE |collectUpper| (|#1| |#1| |#4|))
                       (SIGNATURE |collect| (|#1| |#1| |#4|))
                       (SIGNATURE |collectUnder| (|#1| |#1| |#4|))
                       (SIGNATURE |mainVariable?|
                           ((|Boolean|) |#4| |#1|))
                       (SIGNATURE |mainVariables| ((|List| |#4|) |#1|))
                       (SIGNATURE |variables| ((|List| |#4|) |#1|))
                       (SIGNATURE = ((|Boolean|) |#1| |#1|)))
                   (|PolynomialSetCategory| |#2| |#3| |#4| |#5|)
                   (|Ring|) (|OrderedAbelianMonoidSup|) (|OrderedSet|)
                   (|RecursivePolynomialCategory| |#2| |#3| |#4|))
                 T '|PolynomialSetCategory&|
                 (|put| '|PolynomialSetCategory&| '|mode|
                        '(|Mapping|
                             (CATEGORY |domain|
                                 (SIGNATURE |triangular?|
                                     ((|Boolean|) |#1|))
                                 (SIGNATURE |rewriteIdealWithRemainder|
                                     ((|List| |#5|) (|List| |#5|) |#1|))
                                 (SIGNATURE
                                     |rewriteIdealWithHeadRemainder|
                                     ((|List| |#5|) (|List| |#5|) |#1|))
                                 (SIGNATURE |remainder|
                                     ((|Record| (|:| |rnum| |#2|)
                                       (|:| |polnum| |#5|)
                                       (|:| |den| |#2|))
                                      |#5| |#1|))
                                 (SIGNATURE |headRemainder|
                                     ((|Record| (|:| |num| |#5|)
                                       (|:| |den| |#2|))
                                      |#5| |#1|))
                                 (SIGNATURE |roughUnitIdeal?|
                                     ((|Boolean|) |#1|))
                                 (SIGNATURE |roughEqualIdeals?|
                                     ((|Boolean|) |#1| |#1|))
                                 (SIGNATURE |roughSubIdeal?|
                                     ((|Boolean|) |#1| |#1|))
                                 (SIGNATURE |roughBase?|
                                     ((|Boolean|) |#1|))
                                 (SIGNATURE |trivialIdeal?|
                                     ((|Boolean|) |#1|))
                                 (SIGNATURE |sort|
                                     ((|Record| (|:| |under| |#1|)
                                       (|:| |floor| |#1|)
                                       (|:| |upper| |#1|))
                                      |#1| |#4|))
                                 (SIGNATURE |collectUpper|
                                     (|#1| |#1| |#4|))
                                 (SIGNATURE |collect| (|#1| |#1| |#4|))
                                 (SIGNATURE |collectUnder|
                                     (|#1| |#1| |#4|))
                                 (SIGNATURE |mainVariable?|
                                     ((|Boolean|) |#4| |#1|))
                                 (SIGNATURE |mainVariables|
                                     ((|List| |#4|) |#1|))
                                 (SIGNATURE |variables|
                                     ((|List| |#4|) |#1|))
                                 (SIGNATURE = ((|Boolean|) |#1| |#1|)))
                             (|PolynomialSetCategory| |#2| |#3| |#4|
                                 |#5|)
                             (|Ring|) (|OrderedAbelianMonoidSup|)
                             (|OrderedSet|)
                             (|RecursivePolynomialCategory| |#2| |#3|
                                 |#4|))
                        |$CategoryFrame|)))) 