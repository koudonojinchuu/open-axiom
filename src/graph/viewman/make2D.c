/*
  Copyright (C) 1991-2002, The Numerical Algorithms Group Ltd.
  All rights reserved.
  Copyright (C) 2007-2008, Gabriel Dos Reis.
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are
  met:

      - Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.

      - Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in
        the documentation and/or other materials provided with the
        distribution.

      - Neither the name of The Numerical Algorithms Group Ltd. nor the
        names of its contributors may be used to endorse or promote products
        derived from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
  TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
  PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
  OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

#define _MAKE2D_C
#include "openaxiom-c-macros.h"

#include "viewman.h"

#include "sockio.h"
#include "make2D.H1"

void 
makeView2DFromSpadData(view2DStruct *viewdata,graphStateStruct graphState[])
{

  int i;

  viewdata->title = get_string(spadSock);

  viewdata->vX = get_int(spadSock);
  viewdata->vY = get_int(spadSock);
  viewdata->vW = get_int(spadSock);
  viewdata->vH = get_int(spadSock);

  viewdata->showCP = get_int(spadSock);

  for (i=0; i<maxGraphs; i++) {
    viewdata->graphKeyArray[i] = get_int(spadSock);
    if (viewdata->graphKeyArray[i]) {

      graphState[i].scaleX     = get_float(spadSock);
      graphState[i].scaleY     = get_float(spadSock);
      graphState[i].deltaX     = get_float(spadSock);
      graphState[i].deltaY     = get_float(spadSock);  
      graphState[i].pointsOn   = get_int(spadSock);
      graphState[i].connectOn  = get_int(spadSock);
      graphState[i].splineOn   = get_int(spadSock);
      graphState[i].axesOn     = get_int(spadSock);
      graphState[i].axesColor  = get_int(spadSock);
      graphState[i].unitsOn    = get_int(spadSock);
      graphState[i].unitsColor = get_int(spadSock);
      graphState[i].showing    = get_int(spadSock);
      graphState[i].selected   = 1;                /* always default to selected? */

    }
  }
}
