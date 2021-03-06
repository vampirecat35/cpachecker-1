// This file is part of CPAchecker,
// a tool for configurable software verification:
// https://cpachecker.sosy-lab.org
//
// SPDX-FileCopyrightText: 2007-2020 Dirk Beyer <https://www.sosy-lab.org>
//
// SPDX-License-Identifier: Apache-2.0

/* Generated by CIL v. 1.3.7 */
/* print_CIL_Input is true */

#line 1 "/home/merry/Desktop/function_call2.c"
int compute_square(int y ) 
{ 

  {
#line 3
  return (y + 3);
}
}
#line 6 "/home/merry/Desktop/function_call2.c"
void test1(void) 
{ int z ;

  {
#line 7
  z = 0;
#line 8
  return;
}
}
#line 10 "/home/merry/Desktop/function_call2.c"
int test2(void) 
{ 

  {
#line 11
  return (0);
}
}
#line 14 "/home/merry/Desktop/function_call2.c"
void test3(int a ) 
{ int w ;

  {
#line 15
  w = a + 2;
#line 16
  return;
}
}
#line 18 "/home/merry/Desktop/function_call2.c"
int main(void) 
{ int x ;
  int tmp ;
  int tmp___0 ;

  {
  {
#line 20
  x = 2;
#line 21
  test1();
#line 22
  test2();
#line 23
  tmp = test2();
  }
#line 23
  if (tmp) {
    goto ERROR;
  } else {

  }
  {
#line 27
  test3(x);
#line 28
  compute_square(x);
#line 29
  tmp___0 = compute_square(x);
#line 29
  x = tmp___0 + 2;
  }
#line 30
  if (x != 7) {
    goto ERROR;
  } else {

  }
#line 34
  return (0);
  ERROR: 
#line 36
  return (-1);
}
}
