extern int __VERIFIER_nondet_int();
/* Generated by CIL v. 1.5.1 */
/* print_CIL_Input is true */

#line 1 "<compiler builtins>"
void *__builtin_alloca(unsigned long  ) ;
#line 2 "sendmail_fromqp.c"
int flag  =    0;
#line 5 "sendmail_fromqp.c"
int main(void) 
{ 
  int BASE_SZ ;
  int *outfile ;
  unsigned long __lengthofoutfile ;
  void *tmp ;
  int c1 ;
  int nchar ;
  int out ;
  unsigned long __cil_tmp9 ;
  int *__cil_tmp10 ;
  int *__cil_tmp11 ;
  int *__cil_tmp12 ;

  {
  {
#line 9
  BASE_SZ = 2;
#line 10
  __lengthofoutfile = (unsigned long )BASE_SZ;
#line 10
  __cil_tmp9 = 4UL * __lengthofoutfile;
#line 10
  tmp = __builtin_alloca(__cil_tmp9);
#line 10
  outfile = (int *)tmp;
#line 12
  nchar = 0;
#line 13
  out = 0;
  }
  {
#line 15
  while (1) {
    while_continue: /* CIL Label */ ;
#line 15
    if (__VERIFIER_nondet_int()) {

    } else {
#line 15
      goto while_break;
    }
#line 17
    if (__VERIFIER_nondet_int()) {
#line 20
      if (__VERIFIER_nondet_int()) {
#line 21
        goto while_break;
      } else {

      }
#line 25
      if (__VERIFIER_nondet_int()) {
#line 27
        out = 0;
#line 28
        nchar = 0;
#line 29
        goto while_continue;
      } else {
#line 36
        if (__VERIFIER_nondet_int()) {
#line 37
          goto while_break;
        } else {

        }
#line 39
        nchar = nchar + 1;
#line 40
        if (nchar > BASE_SZ) {
#line 41
          goto while_break;
        } else {

        }
#line 44
        flag = out;
#line 45
        __cil_tmp10 = outfile + out;
#line 45
        *__cil_tmp10 = c1;
#line 46
        out = out + 1;
      }
    } else {
#line 53
      nchar = nchar + 1;
#line 54
      if (nchar > BASE_SZ) {
#line 55
        goto while_break;
      } else {

      }
#line 58
      flag = out;
#line 59
      __cil_tmp11 = outfile + out;
#line 59
      *__cil_tmp11 = c1;
#line 60
      out = out + 1;
#line 62
      if (__VERIFIER_nondet_int()) {
#line 63
        goto while_break;
      } else {

      }
    }
  }
  while_break: /* CIL Label */ ;
  }
#line 68
  flag = out;
#line 69
  __cil_tmp12 = outfile + out;
#line 69
  *__cil_tmp12 = 1;
#line 70
  out = out + 1;
#line 71
  return (0);
}
}
