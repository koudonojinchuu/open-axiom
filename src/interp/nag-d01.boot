-- Copyright (c) 1991-2002, The Numerical ALgorithms Group Ltd.
-- All rights reserved.
-- Copyright (C) 2007, Gabriel Dos Reis.
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
--     - Neither the name of The Numerical ALgorithms Group Ltd. nor the
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


)package "BOOT"

d01ajf() ==
  htInitPage('"D01AJF - 1-D quadrature, adaptive, finite interval, allowing for badly-behaved integrands", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01ajf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01ajf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the integral ")
    (text . "\inputbitmap{\htbmdir{}/integral.bitmap} f(x) dx ")
    (text . "using an adaptive method. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} f to be integrated in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "X*sin(30*X)/(sqrt(1-(X/(2*\%pi))**2))" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "0.0" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "\%pi*2" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01ajfGen)
  htShowPage()

d01ajfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/4
  prefix := STRCONC("d01ajf(",a," ,",b," ,",epsabs," ,",epsrel," ,",STRINGIMAGE lw)
  middle := STRCONC(" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC("(",express,"::Expression Float) :: ASP1(F))")
  linkGen STRCONC(prefix,middle,end)

d01akf() ==
  htInitPage('"D01AKF - 1-D quadrature, adaptive, finite interval, method suitable for oscillating functions", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01akf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01akf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Computes \space{1} \inputbitmap{\htbmdir{}/integral.bitmap} ")
    (text . "f(x) dx using an adaptive method, ")
    (text . "especially suited to oscillating, non-singular integrands. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} f to be integrated in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "X*sin(30*X)*cos(X)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "0.0" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "\%pi*2" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01akfGen)
  htShowPage()

d01akfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/4
  prefix := STRCONC("d01akf(",a," ,",b," ,",epsabs," ,",epsrel," ,",STRINGIMAGE lw)
  middle := STRCONC(" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC("(",express,"::Expression Float) :: ASP1(F))")
  linkGen STRCONC(prefix,middle,end)

d01alf() ==
  htInitPage('"D01ALF - 1-D quadrature, adaptive, finite interval, allowing for singularities at user-specified break-points ", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01alf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01alf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the integral \space{1} ")
    (text . "\inputbitmap{\htbmdir{}/integral.bitmap} f(x) dx; ")
    (text . "the integrand may have local singular behaviour at a ")
    (text . "finite number of points within [a,b]. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} f to be integrated in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "1/sqrt(abs(X-1/7))" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "0.0" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "1.0" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Number of user supplied break-points: \tab{38}")
    (bcStrings (10 "1" npts PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline User supplied break-points (separated by commas): ")
    (text . "\newline \tab{2} ")
    (bcStrings (40 "1/7" points EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01alfGen)
  htShowPage()

d01alfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  npts := 
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'npts)
    objValUnwrap htpLabelSpadValue(htPage, 'npts)
  points := htpLabelInputString(htPage, 'points)
  points := STRCONC ('"[[",points,"]]")
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/2
  prefix := STRCONC('"d01alf(",a," ,",b," ,",STRINGIMAGE npts,",",points,",")
  prefix := STRCONC(prefix,epsabs," ,",epsrel," ,",STRINGIMAGE lw)
  middle := STRCONC('" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC('"(",express,"::Expression Float) :: ASP1(F))")
  linkGen STRCONC(prefix,middle,end)

d01amf() ==
  htInitPage('"D01AMF   1-D quadrature, adaptive, infinite or semi-infinite interval",nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01amf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01amf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "\newline ")
    (text . "Evaluates the integral \space{1} ")
    (text . "\inputbitmap{\htbmdir{}/integral.bitmap} f(x) dx, ")
    (text . "where (a,b) can be an infinite or semi-infinite interval.")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} f to be integrated in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "1/((X+1)*sqrt(X))" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Bound} the finite limit of the integration range: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" a F))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline Choose the kind of integration range: ")
    (radioButtons inf
        (" 1" "\tab{2} Range is [Bound, +infinity] " plus)
        ("-1" "\tab{2} Range is [-infinity, Bound] " minus)
        ("2" "\tab{2} Range is [-infinity, +infinity] (Bound is not used) " minusPlus))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01amfGen)
  htShowPage()

d01amfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  infinity := htpButtonValue(htPage,'inf)
  inf := 
    infinity = 'plus => 1
    infinity = 'minus => -1
    2
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/4
  prefix := STRCONC('"d01amf(",a," ,",STRINGIMAGE inf," ,")
  prefix := STRCONC(prefix,epsabs," ,",epsrel," ,",STRINGIMAGE lw)
  middle := STRCONC('" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC('"(",express,"::Expression Float) :: ASP1(F))")
  linkGen STRCONC(prefix,middle,end)

d01anf() ==
  htInitPage('"D01ANF - 1-D quadrature, adaptive, finite interval, weight function cos(\omega x) or sin(\omega x)", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01anf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01anf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates \inputbitmap{\htbmdir{}/integral.bitmap} g(x)sin(\omega x) dx ")
    (text . "or \inputbitmap{\htbmdir{}/integral.bitmap} g(x)cos(\omega x) dx, ")
    (text . "the sine and cosine transform respectively. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} f to be integrated in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "log(X)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "1.0e-6" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "1.0" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "\omega the weight function:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\tab{34} ")
    (bcStrings (20 "10*\%pi" omega F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Key value, indicates which integral is to be computed:")
    (radioButtons key
        ("" "  sin" sin)
        ("" "  cos" cos))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01anfGen)
  htShowPage()

d01anfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  omega := htpLabelInputString(htPage,'omega)
  type  := htpButtonValue(htPage,'key)
  key :=
    type = 'cos => 1
    2
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/4
  prefix := STRCONC("d01anf(",a," ,",b," ,",omega," ,",STRINGIMAGE key," ,",epsabs," ,",epsrel," ,",STRINGIMAGE lw)
  middle := STRCONC(" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC("(",express,"::Expression Float) :: ASP1(G))")
  linkGen STRCONC(prefix,middle,end)

d01apf() ==
  htInitPage('"D01APF - 1-D quadrature, adaptive, finite interval, weight function with end point singularities of algebraico-logarithmic type", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01apf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01apf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates \inputbitmap{\htbmdir{}/integral.bitmap} g(x)w(x) dx, where w(x) ")
    (text . "has end-point singularities of algebraico-logarithmic type. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the {\em function} g(x) in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "sin(10*X)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "1.0e-6" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "1.0" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline \alpha in the weight function:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "\beta in the weight function:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "-0.5" alpha F))
    (text . "\tab{34} ")
    (bcStrings (10 "-0.5" beta F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Key value, indicates which weight function is to be used: ")
    (radioButtons key
        ("" "\space{1}w(x) = (x-a)**\alpha\space{1}* (b-x)**\beta" kone)
        ("" "\space{1}w(x) = (x-a)**\alpha\space{1}* (b-x)**\beta * ln(x-a)" ktwo)
        ("" "\space{1}w(x) = (x-a)**\alpha\space{1}* (b-x)**\beta * ln(b-x)" kthree)
        ("" "\space{1}w(x) = (x-a)**\alpha\space{1}* (b-x)**\beta * ln(x-a) * ln(b-x) " kfour))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01apfGen)
  htShowPage()

d01apfGen htPage ==
  express := htpLabelInputString(htPage,'expression)
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  alpha := htpLabelInputString(htPage,'alpha)
  beta := htpLabelInputString(htPage,'beta)
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  type  := htpButtonValue(htPage,'key)
  key :=
    type = 'kone => 1
    type = 'ktwo => 2
    type = 'kthree => 3
    4
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  liw := lw/4
  prefix := STRCONC("d01apf(",a," ,",b," ,",alpha," ,",beta," ,")
  prefix := STRCONC(prefix,STRINGIMAGE key," ,",epsabs," ,",epsrel," ,")
  prefix := STRCONC(prefix,STRINGIMAGE lw," ,",STRINGIMAGE liw," ,")
  end := STRCONC("(",express,"::Expression Float) :: ASP1(G))")
  linkGen STRCONC(prefix,STRINGIMAGE ifail," ,",end)

d01aqf() ==
  htInitPage('"D01AQF - 1-D quadrature, adaptive, finite interval, weight function 1/(x-c), Cauchy principal value (Hilbert transform)",nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01aqf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01aqf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the Hilbert transform \inputbitmap{\htbmdir{}/integral.bitmap}")
    (text . "g(x)/(x-c) dx.")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the function {\it g(x)} in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (55 "(X**2+0.01**2)**-1" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval {\it a}: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound {\it b}:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "-1.0" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "1.0" b F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} \newline ")
    (text . "Parameter {\it c} \notequal {\it a} or {\it b}: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.5" c F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\tab{32} \menuitemstyle{}\tab{34}")
    (text . "Relative accuracy:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0" epsabs F))
    (text . "\tab{34} ")
    (bcStrings (10 "1.0e-4" epsrel F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01aqfGen)
  htShowPage()

d01aqfGen htPage ==
  express := htpLabelInputString(htPage,'expression)
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  c := htpLabelInputString(htPage,'c)
  epsabs := htpLabelInputString(htPage,'epsabs)
  epsrel := htpLabelInputString(htPage,'epsrel)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  liw := lw/4
  prefix := STRCONC("d01aqf(",a," ,",b," ,",c," ,",epsabs," ,",epsrel," ,")
  prefix := STRCONC(prefix,STRINGIMAGE lw," ,",STRINGIMAGE liw," ,")
  end := STRCONC("((",express,")::Expression Float) :: ASP1(G))")
  linkGen STRCONC(prefix,STRINGIMAGE ifail," ,",end)

d01asf() ==
  htInitPage('"D01ASF - 1-D quadrature, adaptive, semi-infinite interval, weight function cos(\omega x) or sin(\omega x)", nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01asf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01asf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates \inputbitmap{\htbmdir{}/si-integral.bitmap} ")
    (text . "g(x)sin(\omega x) dx ")
    (text . "or \inputbitmap{\htbmdir{}/si-integral.bitmap} ")
    (text . "g(x)cos(\omega x) dx, ")
    (text . "the sine and cosine transform respectively. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the function {\it g(x)} in terms of X: ")
    (text . "\newline \tab{2} ")
    (bcStrings (45 "1/sqrt(X)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "1.0e-12" a F))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "Parameter \omega in the weight function of the transform: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "\%pi/2" omega F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Absolute accuracy required:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "1.0e-3" epsabs F))
    (text . "\newline \menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 800 lw PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "{\it LIMLST} upper bound on number of intervals:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 50 limlst PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Key value, indicates which integral is to be computed:")
    (radioButtons key
        ("" "  cos(\omega x)" cos)
        ("" "  sin(\omega x)" sin))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01asfGen)
  htShowPage()

d01asfGen htPage ==
  lw :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lw)
    objValUnwrap htpLabelSpadValue(htPage, 'lw)
  limlst :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'limlst)
    objValUnwrap htpLabelSpadValue(htPage, 'limlst)
  a := htpLabelInputString(htPage,'a)
  epsabs := htpLabelInputString(htPage,'epsabs)
  omega := htpLabelInputString(htPage,'omega)
  type  := htpButtonValue(htPage,'key)
  key :=
    type = 'cos => 1
    2
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  express := htpLabelInputString(htPage,'expression)
  liw := lw/2
  prefix := STRCONC("d01asf(",a," ,",omega," ,",STRINGIMAGE key," ,",epsabs)
  prefix := STRCONC(prefix," ,",STRINGIMAGE limlst," ,",STRINGIMAGE lw)
  middle := STRCONC(" ,",STRINGIMAGE liw," ,",STRINGIMAGE ifail," ,")
  end := STRCONC("(",express,"::Expression Float) :: ASP1(G))")
  linkGen STRCONC(prefix,middle,end)



d01gaf() ==
  htInitPage('"D01GAF - \space{1} 1-D quadrature, integration of function defined by data values, Gill-Miller method", nil)
  htMakePage '(
    (domainConditions 
       (isDomain PI (PositiveInteger)))
    (text . "\windowlink{Manual Page}{manpageXXd01gaf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01gaf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the integral ")
    (text . "\inputbitmap{\htbmdir{}/d01gaf1.bitmap} y(x)dx ")
    (text . "where the numerical value of the function {\em y} is ")
    (text . "specified at the n distinct points \vspace{-26} ")
    (text . "\inputbitmap{\htbmdir{}/d01gaf2.bitmap} ")
    (text . "\blankline ")
    (text . "Enter the number of points:")
    (text . "\newline\tab{2} ")
    (bcStrings (5 21 n PI))
    (text . "\blankline ")
    (text . "\newline Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01gafSolve)
  htShowPage()

d01gafSolve htPage ==
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  n = '21 => d01gafDefaultSolve(htPage,ifail)
  labelList :=
    "append"/[f(i) for i in 1..n] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      post   := ('"\tab{40} ")
      xnam := INTERN STRCONC ('"x",STRINGIMAGE i)
      ynam := INTERN STRCONC ('"y",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[10, 0.0, xnam, 'F]], 
       ['text,:post],['bcStrings,[10, 0.0, ynam, 'F]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain F (Float))
           (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage("D01GAF - 1-D quadrature, integration of function defined by data values", htpPropertyList htPage)
  htSay '"\menuitemstyle{}\tab{2} Enter values for {\em x}: \tab{38} "
  htSay '"\menuitemstyle{}\tab{40} Enter values for {\em y}: "
  htMakePage equationPart
  htSay '"\blankline "
  htSay '"Note:\space{1}{\em x} values in ascending or descending order only "
  htMakeDoneButton('"Continue",'d01gafGen)
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

d01gafDefaultSolve (htPage, ifail) ==
  n := '21
  page := htInitPage('"D01GAF - 1-D quadrature, integration of function defined by data values",htpPropertyList htPage)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} Enter values for {\em x}: \tab{38} ")
    (text . "\menuitemstyle{}\tab{40} Enter values for {\em y}: ")
    (text . "\newline \tab{2}")
    (bcStrings (10 "0.00" x1 F))
    (text . "\tab{40} ")
    (bcStrings (10 "4.0000" y1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.04" x2 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.9936" y2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.08" x3 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.9746" y3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.12" x4 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.9432" y4 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.22" x5 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.8153" y5 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.26" x6 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.7467" y6 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.30" x7 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.6697" y7 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.38" x8 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.4943" y8 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.39" x9 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.4719" y9 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.42" x10 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.4002" y10 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.45" x11 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.3264" y11 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.46" x12 F))
    (text . "\tab{40} ")
    (bcStrings (10 "3.3014" y12 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.60" x13 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.9412" y13 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.68" x14 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.7352" y14 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.72" x15 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.6344" y15 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.73" x16 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.6094" y16 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.83" x17 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.3684" y17 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.85" x18 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.3222" y18 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.88" x19 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.2543" y19 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.90" x20 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.2099" y20 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "1.00" x21 F))
    (text . "\tab{40} ")
    (bcStrings (10 "2.0000" y21 F))
    (text . "\newline \tab{2} ")
    (text . "\blankline ")
    (text . "Note:\space{1}{\em x} values in ascending or descending order only ")
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'d01gafGen)      
  htpSetProperty(page,'n,n)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()


d01gafGen htPage ==
  n := htpProperty(htPage,'n)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := STRCONC ((first y).1," ")
    y := rest y
    left :=  STRCONC ((first y).1," ")
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  realstring := bcwords2liststring reallist
  imagstring := bcwords2liststring imaglist
  linkGen STRCONC ('"d01gaf([",realstring,"],[",imagstring,"],",STRINGIMAGE n,",", STRINGIMAGE ifail,")")

d01fcf() ==
  htInitPage('"D01FCF - Multi-dimensional adaptive quadrature over hyper-rectangle",nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01fcf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01fcf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the multi-dimensional integral ")
    (text . "\center{\htbitmap{d01fcf}}")
    (text . "with constant finite limits, using an adaptive subdivision ")
    (text . "strategy.")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Number of dimensions n in the integral, 2 \htbitmap{less=} ")
    (text . "{\it NDIM} \htbitmap{less=} 15: ")
    (text . "\newline\tab{2} ")
    (bcStrings (6 4 ndim F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the integrand {\it f} in terms of X[1]...X[n]: ")
    (text . "\newline ")
    (bcStrings (58 "4.0*X[1]*X[3]*X[3]*exp(2.0*X[1]*X[3])/((1.0+X[2]+X[4])**2)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline Minimum number of evaluations: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "Maximum number of evaluations: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 1000 minpts PI))
    (text . "\tab{34} ")
    (bcStrings (10 5700 maxpts PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Relative accuracy required:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.0001" eps F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 606 lenwrk PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01fcfSolve)
  htShowPage()


d01fcfSolve htPage ==
  ndim :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ndim)
    objValUnwrap htpLabelSpadValue(htPage, 'ndim)
  expression := htpLabelInputString(htPage,'expression)
  minpts :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'minpts)
    objValUnwrap htpLabelSpadValue(htPage, 'minpts)
  maxpts :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'maxpts)
    objValUnwrap htpLabelSpadValue(htPage, 'maxpts)
  eps := htpLabelInputString(htPage,'eps)
  lenwrk :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lenwrk)
    objValUnwrap htpLabelSpadValue(htPage, 'lenwrk)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ndim = '4 => d01fcfDefaultSolve(htPage,minpts,maxpts,eps,lenwrk,expression,ifail)
  labelList :=
    "append"/[f(i) for i in 1..ndim] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      post   := ('"\tab{32} ")
      rnam := INTERN STRCONC ('"a",STRINGIMAGE i)
      inam := INTERN STRCONC ('"b",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[10, 0.0, rnam, 'P]], 
       ['text,:post],['bcStrings,[10, 1.0, inam, 'P]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage('"D01FCF - Multi-dimensional adaptive quadrature over hyper-rectangle",nil)
  htSay '"Please enter the limits of integration:- "
  htSay '"\blankline "
  htSay '"\menuitemstyle{}\tab{2} Lower limits: \tab{30} "
  htSay '"\menuitemstyle{}\tab{32} Upper limits: "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'d01fcfGen)
  htpSetProperty(page,'ndim,ndim)
  htpSetProperty(page,'expression,expression)
  htpSetProperty(page,'minpts,minpts)
  htpSetProperty(page,'maxpts,maxpts)
  htpSetProperty(page,'eps,eps)
  htpSetProperty(page,'lenwrk,lenwrk)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

d01fcfDefaultSolve(htPage,minpts,maxpts,eps,lenwrk,expression,ifail) ==
  ndim := '4
  page := htInitPage('"D01FCF - Multi-dimensional adaptive quadrature over hyper-rectangle",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "Please enter the limits of integration:- ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} \tab{2} ")
    (text . "Lower limits: \tab{30} ")
    (text . "\menuitemstyle{} \tab{32} Upper limits: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a1 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a2 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a3 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a4 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b4 F))
    (text . "\newline ")
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'d01fcfGen)      
  htpSetProperty(page,'ndim,ndim)
  htpSetProperty(page,'expression,expression)
  htpSetProperty(page,'minpts,minpts)
  htpSetProperty(page,'maxpts,maxpts)
  htpSetProperty(page,'eps,eps)
  htpSetProperty(page,'lenwrk,lenwrk)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

d01fcfGen htPage ==
  ndim := htpProperty(htPage,'ndim)
  minpts := htpProperty(htPage,'minpts)
  maxpts := htpProperty(htPage,'maxpts)
  eps := htpProperty(htPage,'eps)
  lenwrk := htpProperty(htPage,'lenwrk)
  expression := htpProperty(htPage,'expression)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := (first y).1
    y := rest y
    left := (first y).1
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  astring := bcwords2liststring reallist
  bstring := bcwords2liststring imaglist
  prefix := STRCONC("d01fcf(",STRINGIMAGE ndim,", [",astring,"],[",bstring,"], ")
  middle := STRCONC(STRINGIMAGE maxpts,", ",eps," ,",STRINGIMAGE lenwrk," ,")
  middle := STRCONC(middle,STRINGIMAGE minpts," ,",STRINGIMAGE ifail," ,")
  end := STRCONC("(",expression,"::Expression Float) :: ASP4(FUNCTN))")
  linkGen STRCONC(prefix,middle,end)


d01gbf() ==
  htInitPage('"D01GBF - Multi-dimensional quadrature over hyper-rectangle, Monte Carlo method",nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01gbf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01gbf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Evaluates the multidimensional integral ")
    (text . "\center{\htbitmap{d01fcf}} with constant finite limits, ")
    (text . "using an adaptive Monte-Carlo method;")
    (text . " the routine is suitable for low accuracy work. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Number of dimensions n in the integral, {\it NDIM}:")
    (text . "\newline\tab{2} ")
    (bcStrings (6 4 ndim F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the integrand {\it f} in terms of X[1]...X[n]: ")
    (text . "\newline ")
    (bcStrings (60 "4.0*X[1]*X[3]*X[3]*exp(2.0*X[1]*X[3])/((1.0+X[2]+X[4])**2)" expression EM))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline Minimum number of FUNCTN calls: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "Maximum number of FUNCTN calls: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 1000 mincls PI))
    (text . "\tab{34} ")
    (bcStrings (10 20000 maxcls PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Relative accuracy required:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "0.01" eps F))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Dimension of workspace array: ")
    (text . "\newline\tab{2} ")
    (bcStrings (10 500 lenwrk PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01gbfSolve)
  htShowPage()


d01gbfSolve htPage ==
  ndim :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'ndim)
    objValUnwrap htpLabelSpadValue(htPage, 'ndim)
  expression := htpLabelInputString(htPage,'expression)
  mincls :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'mincls)
    objValUnwrap htpLabelSpadValue(htPage, 'mincls)
  maxcls :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'maxcls)
    objValUnwrap htpLabelSpadValue(htPage, 'maxcls)
  eps := htpLabelInputString(htPage,'eps)
  lenwrk :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'lenwrk)
    objValUnwrap htpLabelSpadValue(htPage, 'lenwrk)
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => '1
    '-1
  ndim = '4 => d01gbfDefaultSolve(htPage,mincls,maxcls,eps,lenwrk,expression,ifail)
  labelList :=
    "append"/[f(i) for i in 1..ndim] where f(i) ==
      prefix := ('"\newline \tab{2} ")
      post   := ('"\tab{32} ")
      rnam := INTERN STRCONC ('"a",STRINGIMAGE i)
      inam := INTERN STRCONC ('"b",STRINGIMAGE i)
      [['text,:prefix],['bcStrings,[10, 0.0, rnam, 'P]], 
       ['text,:post],['bcStrings,[10, 1.0, inam, 'P]]]
  equationPart := [
     '(domainConditions 
        (isDomain P (Polynomial $EmptyMode))
         (isDomain S (String))
          (isDomain PI (PositiveInteger))),
            :labelList]
  page := htInitPage('"D01GBF - Multi-dimensional quadrature over hyper-rectangle, Monte Carlo method",nil)
  htSay '"Please enter the limits of integration:- "
  htSay '"\blankline "
  htSay '"\menuitemstyle{}\tab{2} Lower limits: \tab{30} "
  htSay '"\menuitemstyle{}\tab{32} Upper limits: "
  htMakePage equationPart
  htMakeDoneButton('"Continue",'d01gbfGen)
  htpSetProperty(page,'ndim,ndim)
  htpSetProperty(page,'expression,expression)
  htpSetProperty(page,'mincls,mincls)
  htpSetProperty(page,'maxcls,maxcls)
  htpSetProperty(page,'eps,eps)
  htpSetProperty(page,'lenwrk,lenwrk)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()

d01gbfDefaultSolve(htPage,mincls,maxcls,eps,lenwrk,expression,ifail) ==
  ndim := '4
  page := htInitPage('"D01GBF - Multi-dimensional quadrature over hyper-rectangle, Monte Carlo method",nil)
  htMakePage '(
    (domainConditions 
       (isDomain F (Float)))
    (text . "\newline ")
    (text . "Please enter the limits of integration:- ")
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{} \tab{2} ")
    (text . "Lower limits: \tab{30} ")
    (text . "\menuitemstyle{} \tab{32} Upper limits: ")
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a1 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b1 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a2 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b2 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a3 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b3 F))
    (text . "\newline \tab{2} ")
    (bcStrings (10 "0.0" a4 F))
    (text . "\tab{32} ")
    (bcStrings (10 "1.0" b4 F))
    (text . "\newline ")
    (text . "\blankline"))
  htMakeDoneButton('"Continue",'d01gbfGen)      
  htpSetProperty(page,'ndim,ndim)
  htpSetProperty(page,'expression,expression)
  htpSetProperty(page,'mincls,mincls)
  htpSetProperty(page,'maxcls,maxcls)
  htpSetProperty(page,'eps,eps)
  htpSetProperty(page,'lenwrk,lenwrk)
  htpSetProperty(page,'ifail,ifail)
  htpSetProperty(page,'inputArea, htpInputAreaAlist htPage)
  htShowPage()




d01gbfGen htPage ==
  ndim := htpProperty(htPage,'ndim)
  mincls := htpProperty(htPage,'mincls)
  maxcls := htpProperty(htPage,'maxcls)
  eps := htpProperty(htPage,'eps)
  lenwrk := htpProperty(htPage,'lenwrk)
  expression := htpProperty(htPage,'expression)
  ifail := htpProperty(htPage,'ifail)
  alist := htpInputAreaAlist htPage
  y := alist
  while y repeat
    right := (first y).1
    y := rest y
    left := (first y).1
    y := rest y
    reallist := [left,:reallist]
    imaglist := [right,:imaglist]
  astring := bcwords2liststring reallist
  bstring := bcwords2liststring imaglist
  prefix := STRCONC("d01gbf(",STRINGIMAGE ndim,", [",astring,"],[",bstring,"], ")
  middle := STRCONC(STRINGIMAGE maxcls,", ",eps," ,",STRINGIMAGE lenwrk," ,")
  middle := STRCONC(middle,STRINGIMAGE mincls," ,[[0.0 for i in 1..")
  middle := STRCONC(middle,STRINGIMAGE lenwrk,"]],",STRINGIMAGE ifail," ,")
  end := STRCONC("(",expression,"::Expression Float) :: ASP4(FUNCTN))")
  linkGen STRCONC(prefix,middle,end)

d01bbf() ==
  htInitPage('"D01BBF - Weights and abscissae for Gaussian quadrature rules",nil)
  htMakePage '(
    (domainConditions
        (isDomain EM $EmptyMode)
        (isDomain PI (PositiveInteger))
        (isDomain F (Float)))
    (text . "\windowlink{Manual Page}{manpageXXd01bbf} for this routine ")
    (text . "\newline ")
    (text . "\lispwindowlink{Browser operation page}{(|oPageFrom| '|d01bbf| '|NagIntegrationPackage|)} for this routine")
    (text . "\newline \horizontalline ")
    (text . "Returns the weights and abscissae appropriate to a Gaussian ")
    (text . "quadrature formula with a specified number of abscissae. ")
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Enter the D01XXX subroutine: ")
    (radioButtons gtype
        ("" " D01BAZ" gZero)
        ("" " D01BAY" gOne)
        ("" " D01BAX" gTwo)
        ("" " D01BAW" gThree))
    (text . "\blankline ")
    (text . "\newline \menuitemstyle{}\tab{2} ")
    (text . "\newline {\em Lower} bound of the interval: ")
    (text . "\tab{32} \menuitemstyle{}\tab{34} ")
    (text . "{\em Upper} bound:")
    (text . "\newline\tab{2} ")
    (bcStrings (20 "0.0" a F))
    (text . "\tab{34} ")
    (bcStrings (20 "1.0" b EM))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2} ")
    (text . "Type of weights for Gauss-Laguerre or Gauss-Hermite quadrature:")
    (radioButtons itype
        ("" " adjusted weights" iOne)
        ("" " normal weights" iZero))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "\newline Number of weights & abscissae to be used {\em n}:")
    (text . "\newline\tab{2} ")
    (bcStrings (10 "6" n PI))
    (text . "\blankline ")
    (text . "\newline ")
    (text . "\menuitemstyle{}\tab{2}")
    (text . "Ifail value:")
    (radioButtons ifail
        ("" " -1, Print error messages" minusOne)
        ("" "  1, Suppress error messages" one)))
  htMakeDoneButton('"Continue", 'd01bbfGen)
  htShowPage()

d01bbfGen htPage ==
  sub := htpButtonValue(htPage,'gtype)
  gtype := 
    sub = 'gZero => 0
    sub = 'gOne => 1
    sub = 'gTwo => 2
    3
  a := htpLabelInputString(htPage,'a)
  b := htpLabelInputString(htPage,'b)
  n :=
    $bcParseOnly => PARSE_-INTEGER htpLabelInputString(htPage, 'n)
    objValUnwrap htpLabelSpadValue(htPage, 'n)
  wgts := htpButtonValue(htPage,'itype)
  itype := 
    wgts = 'iOne => 1
    0
  error := htpButtonValue(htPage,'ifail)
  ifail :=
    error = 'one => 1
    -1
  prefix := STRCONC("d01bbf(",a," ,",b," ,",STRINGIMAGE itype," ,")
  end := STRCONC(STRINGIMAGE n," ,",STRINGIMAGE gtype," ,",STRINGIMAGE ifail,")")
  linkGen STRCONC(prefix,end)
