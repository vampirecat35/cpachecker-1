/* Generated by CIL v. 1.3.7 */
/* print_CIL_Input is true */

#line 211 "/usr/lib/gcc/x86_64-linux-gnu/4.4.3/include/stddef.h"
typedef unsigned long size_t;
#line 34 "/usr/include/bits/types.h"
typedef unsigned long __u_long;
#line 135 "/usr/include/bits/types.h"
typedef unsigned int __uid_t;
#line 136 "/usr/include/bits/types.h"
typedef unsigned int __gid_t;
#line 149 "/usr/include/bits/types.h"
typedef long __time_t;
#line 39 "/usr/include/pwd.h"
typedef __gid_t gid_t;
#line 44 "/usr/include/pwd.h"
typedef __uid_t uid_t;
#line 50 "/usr/include/pwd.h"
struct passwd {
   char *pw_name ;
   char *pw_passwd ;
   __uid_t pw_uid ;
   __gid_t pw_gid ;
   char *pw_gecos ;
   char *pw_dir ;
   char *pw_shell ;
};
#line 38 "/usr/include/sys/types.h"
typedef __u_long u_long;
#line 76 "/usr/include/time.h"
typedef __time_t time_t;
#line 77 "sendmail-bad.h"
struct address {
   char *q_paddr ;
   char *q_user ;
   char *q_ruser ;
   char *q_host ;
   u_long q_flags ;
   uid_t q_uid ;
   gid_t q_gid ;
   char *q_home ;
   char *q_fullname ;
   struct address *q_next ;
   struct address *q_alias ;
   char *q_owner ;
   struct address *q_tchain ;
   char *q_orcpt ;
   char *q_status ;
   char *q_rstatus ;
   time_t q_statdate ;
   char *q_statmta ;
   short q_specificity ;
};
#line 100 "sendmail-bad.h"
typedef struct address ADDRESS;
#line 76 "recipient-bad.c"
enum bool {
    false = 0,
    true = 1
} ;
#line 339 "/usr/include/stdio.h"
extern int printf(char const   * __restrict  __format  , ...) ;
#line 73 "/usr/include/pwd.h"
extern void setpwent(void) ;
#line 85
extern struct passwd *getpwent(void) ;
#line 117
extern struct passwd *getpwnam(char const   *__name ) ;
#line 488 "/usr/include/stdlib.h"
extern  __attribute__((__nothrow__)) void free(void *__ptr ) ;
#line 127 "/usr/include/string.h"
extern  __attribute__((__nothrow__)) char *strcpy(char * __restrict  __dest , char const   * __restrict  __src )  __attribute__((__nonnull__(1,2))) ;
#line 142
extern  __attribute__((__nothrow__)) int strcmp(char const   *__s1 , char const   *__s2 )  __attribute__((__pure__,
__nonnull__(1,2))) ;
#line 233
extern  __attribute__((__nothrow__)) char *strchr(char const   *__s , int __c )  __attribute__((__pure__,
__nonnull__(1))) ;
#line 397
extern  __attribute__((__nothrow__)) size_t strlen(char const   *__s )  __attribute__((__pure__,
__nonnull__(1))) ;
#line 534
extern  __attribute__((__nothrow__)) int strcasecmp(char const   *__s1 , char const   *__s2 )  __attribute__((__pure__,
__nonnull__(1,2))) ;
#line 81 "/usr/include/ctype.h"
extern  __attribute__((__nothrow__)) unsigned short const   **__ctype_b_loc(void)  __attribute__((__const__)) ;
#line 116
extern  __attribute__((__nothrow__)) int tolower(int __c ) ;
#line 71 "sendmail-bad.h"
int MaxAliasRecursion ;
#line 72
extern char *xalloc(int  ) ;
#line 73
extern void buildfname(char * , char * , char * ) ;
#line 102
ADDRESS *recipient(ADDRESS *a , ADDRESS **sendq , int aliaslevel ) ;
#line 77 "recipient-bad.c"
enum bool MatchGecos  =    (enum bool )1;
#line 78 "recipient-bad.c"
char SpaceSub  =    (char )' ';
#line 79 "recipient-bad.c"
int MaxAliasRecursion  =    0;
#line 155
struct passwd *finduser(char *name , enum bool *fuzzyp ) ;
#line 104 "recipient-bad.c"
ADDRESS *recipient(ADDRESS *a , ADDRESS **sendq , int aliaslevel ) 
{ register char *p ;
  enum bool quoted ;
  int i ;
  char *buf ;
  char buf0[5] ;
  size_t tmp ;
  enum bool fuzzy ;
  register struct passwd *pw ;
  char test_buf[10] ;
  char nbuf[5] ;
  size_t tmp___0 ;
  char *tmp___1 ;
  int tmp___2 ;
  size_t tmp___3 ;
  char *tmp___4 ;
  size_t tmp___5 ;
  char *tmp___6 ;
  unsigned long __cil_tmp21 ;
  unsigned long __cil_tmp22 ;
  unsigned long __cil_tmp23 ;
  unsigned long __cil_tmp24 ;
  u_long __cil_tmp25 ;
  unsigned long __cil_tmp26 ;
  unsigned long __cil_tmp27 ;
  char const   * __restrict  __cil_tmp28 ;
  unsigned long __cil_tmp29 ;
  unsigned long __cil_tmp30 ;
  char *__cil_tmp31 ;
  char const   *__cil_tmp32 ;
  unsigned long __cil_tmp33 ;
  int __cil_tmp34 ;
  unsigned long __cil_tmp35 ;
  unsigned long __cil_tmp36 ;
  char * __restrict  __cil_tmp37 ;
  unsigned long __cil_tmp38 ;
  unsigned long __cil_tmp39 ;
  char *__cil_tmp40 ;
  char const   * __restrict  __cil_tmp41 ;
  char __cil_tmp42 ;
  int __cil_tmp43 ;
  unsigned int __cil_tmp44 ;
  char __cil_tmp45 ;
  int __cil_tmp46 ;
  char const   * __restrict  __cil_tmp47 ;
  char const   * __restrict  __cil_tmp48 ;
  void *__cil_tmp49 ;
  unsigned long __cil_tmp50 ;
  unsigned long __cil_tmp51 ;
  char const   * __restrict  __cil_tmp52 ;
  unsigned long __cil_tmp53 ;
  unsigned long __cil_tmp54 ;
  unsigned long __cil_tmp55 ;
  unsigned long __cil_tmp56 ;
  u_long __cil_tmp57 ;
  unsigned long __cil_tmp58 ;
  unsigned long __cil_tmp59 ;
  unsigned long __cil_tmp60 ;
  unsigned long __cil_tmp61 ;
  char *__cil_tmp62 ;
  char * __restrict  __cil_tmp63 ;
  char const   * __restrict  __cil_tmp64 ;
  unsigned long __cil_tmp65 ;
  unsigned long __cil_tmp66 ;
  char *__cil_tmp67 ;
  char const   *__cil_tmp68 ;
  unsigned long __cil_tmp69 ;
  unsigned long __cil_tmp70 ;
  unsigned long __cil_tmp71 ;
  unsigned long __cil_tmp72 ;
  char *__cil_tmp73 ;
  char const   *__cil_tmp74 ;
  size_t __cil_tmp75 ;
  int __cil_tmp76 ;
  unsigned long __cil_tmp77 ;
  unsigned long __cil_tmp78 ;
  char * __restrict  __cil_tmp79 ;
  unsigned long __cil_tmp80 ;
  unsigned long __cil_tmp81 ;
  char *__cil_tmp82 ;
  char const   * __restrict  __cil_tmp83 ;
  unsigned long __cil_tmp84 ;
  unsigned long __cil_tmp85 ;
  unsigned long __cil_tmp86 ;
  unsigned long __cil_tmp87 ;
  unsigned long __cil_tmp88 ;
  unsigned long __cil_tmp89 ;
  unsigned long __cil_tmp90 ;
  unsigned long __cil_tmp91 ;
  char *__cil_tmp92 ;
  char const   *__cil_tmp93 ;
  size_t __cil_tmp94 ;
  int __cil_tmp95 ;
  unsigned long __cil_tmp96 ;
  unsigned long __cil_tmp97 ;
  char * __restrict  __cil_tmp98 ;
  char *__cil_tmp99 ;
  char const   * __restrict  __cil_tmp100 ;
  unsigned long __cil_tmp101 ;
  unsigned long __cil_tmp102 ;
  unsigned long __cil_tmp103 ;
  unsigned long __cil_tmp104 ;
  u_long __cil_tmp105 ;
  char const   * __restrict  __cil_tmp106 ;
  unsigned long __cil_tmp107 ;
  unsigned long __cil_tmp108 ;
  char *__cil_tmp109 ;
  char *__cil_tmp110 ;
  char const   * __restrict  __cil_tmp111 ;
  unsigned long __cil_tmp112 ;
  unsigned long __cil_tmp113 ;
  char *__cil_tmp114 ;
  unsigned long __cil_tmp115 ;
  unsigned long __cil_tmp116 ;
  char *__cil_tmp117 ;
  char *__cil_tmp118 ;
  unsigned long __cil_tmp119 ;
  unsigned long __cil_tmp120 ;
  char *__cil_tmp121 ;
  char const   * __restrict  __cil_tmp122 ;
  unsigned long __cil_tmp123 ;
  unsigned long __cil_tmp124 ;
  char *__cil_tmp125 ;
  unsigned long __cil_tmp126 ;
  unsigned long __cil_tmp127 ;
  char __cil_tmp128 ;
  int __cil_tmp129 ;
  unsigned long __cil_tmp130 ;
  unsigned long __cil_tmp131 ;
  char *__cil_tmp132 ;
  char const   *__cil_tmp133 ;
  size_t __cil_tmp134 ;
  int __cil_tmp135 ;
  unsigned long __cil_tmp136 ;
  unsigned long __cil_tmp137 ;
  char * __restrict  __cil_tmp138 ;
  unsigned long __cil_tmp139 ;
  unsigned long __cil_tmp140 ;
  char *__cil_tmp141 ;
  char const   * __restrict  __cil_tmp142 ;
  char const   * __restrict  __cil_tmp143 ;
  unsigned long __cil_tmp144 ;
  unsigned long __cil_tmp145 ;
  char *__cil_tmp146 ;
  unsigned long __cil_tmp147 ;
  unsigned long __cil_tmp148 ;
  unsigned long __cil_tmp149 ;
  unsigned long __cil_tmp150 ;
  u_long __cil_tmp151 ;
  unsigned long __cil_tmp152 ;
  unsigned long __cil_tmp153 ;
  char *__cil_tmp154 ;
  unsigned long __cil_tmp155 ;
  unsigned long __cil_tmp156 ;
  void *__cil_tmp157 ;

  {
#line 112
  quoted = (enum bool )0;
#line 118
  if (aliaslevel == 0) {
#line 119
    __cil_tmp21 = (unsigned long )a;
#line 119
    __cil_tmp22 = __cil_tmp21 + 32;
#line 119
    __cil_tmp23 = (unsigned long )a;
#line 119
    __cil_tmp24 = __cil_tmp23 + 32;
#line 119
    __cil_tmp25 = *((u_long *)__cil_tmp24);
#line 119
    *((u_long *)__cil_tmp22) = __cil_tmp25 | 8UL;
  } else {

  }
#line 123
  if (aliaslevel > MaxAliasRecursion) {
    {
#line 125
    __cil_tmp26 = (unsigned long )a;
#line 125
    __cil_tmp27 = __cil_tmp26 + 104;
#line 125
    *((char **)__cil_tmp27) = (char *)"5.4.6";
#line 126
    __cil_tmp28 = (char const   * __restrict  )"554 aliasing/forwarding loop broken (%d aliases deep; %d max";
#line 126
    printf(__cil_tmp28, aliaslevel, MaxAliasRecursion);
    }
#line 128
    return (a);
  } else {

  }
  {
#line 136
  __cil_tmp29 = (unsigned long )a;
#line 136
  __cil_tmp30 = __cil_tmp29 + 8;
#line 136
  __cil_tmp31 = *((char **)__cil_tmp30);
#line 136
  __cil_tmp32 = (char const   *)__cil_tmp31;
#line 136
  tmp = strlen(__cil_tmp32);
#line 136
  i = (int )tmp;
  }
  {
#line 137
  __cil_tmp33 = (unsigned long )i;
#line 137
  if (__cil_tmp33 >= 5UL) {
    {
#line 138
    __cil_tmp34 = i + 1;
#line 138
    buf = xalloc(__cil_tmp34);
    }
  } else {
#line 140
    __cil_tmp35 = 0 * 1UL;
#line 140
    __cil_tmp36 = (unsigned long )(buf0) + __cil_tmp35;
#line 140
    buf = (char *)__cil_tmp36;
  }
  }
  {
#line 141
  __cil_tmp37 = (char * __restrict  )buf;
#line 141
  __cil_tmp38 = (unsigned long )a;
#line 141
  __cil_tmp39 = __cil_tmp38 + 8;
#line 141
  __cil_tmp40 = *((char **)__cil_tmp39);
#line 141
  __cil_tmp41 = (char const   * __restrict  )__cil_tmp40;
#line 141
  strcpy(__cil_tmp37, __cil_tmp41);
#line 143
  p = buf;
  }
  {
#line 143
  while (1) {
    while_continue: /* CIL Label */ ;
    {
#line 143
    __cil_tmp42 = *p;
#line 143
    __cil_tmp43 = (int )__cil_tmp42;
#line 143
    if (__cil_tmp43 != 0) {
      {
#line 143
      __cil_tmp44 = (unsigned int )quoted;
#line 143
      if (! __cil_tmp44) {

      } else {
#line 143
        goto while_break;
      }
      }
    } else {
#line 143
      goto while_break;
    }
    }
    {
#line 145
    __cil_tmp45 = *p;
#line 145
    __cil_tmp46 = (int )__cil_tmp45;
#line 145
    if (__cil_tmp46 == 92) {
#line 146
      quoted = (enum bool )1;
    } else {

    }
    }
#line 143
    p = p + 1;
  }
  while_break: /* CIL Label */ ;
  }
  {
#line 157
  __cil_tmp47 = (char const   * __restrict  )"Inside TRUE block!!\n";
#line 157
  printf(__cil_tmp47);
#line 160
  __cil_tmp48 = (char const   * __restrict  )"buf used in finduser = %s\n";
#line 160
  printf(__cil_tmp48, buf);
#line 161
  pw = finduser(buf, & fuzzy);
  }
  {
#line 162
  __cil_tmp49 = (void *)0;
#line 162
  __cil_tmp50 = (unsigned long )__cil_tmp49;
#line 162
  __cil_tmp51 = (unsigned long )pw;
#line 162
  if (__cil_tmp51 == __cil_tmp50) {
    {
#line 164
    __cil_tmp52 = (char const   * __restrict  )"Name was not found!\n";
#line 164
    printf(__cil_tmp52);
#line 165
    __cil_tmp53 = (unsigned long )a;
#line 165
    __cil_tmp54 = __cil_tmp53 + 32;
#line 165
    __cil_tmp55 = (unsigned long )a;
#line 165
    __cil_tmp56 = __cil_tmp55 + 32;
#line 165
    __cil_tmp57 = *((u_long *)__cil_tmp56);
#line 165
    *((u_long *)__cil_tmp54) = __cil_tmp57 | 2UL;
#line 166
    __cil_tmp58 = (unsigned long )a;
#line 166
    __cil_tmp59 = __cil_tmp58 + 104;
#line 166
    *((char **)__cil_tmp59) = (char *)"5.1.1";
    }
  } else {
    {
#line 173
    __cil_tmp60 = 0 * 1UL;
#line 173
    __cil_tmp61 = (unsigned long )(test_buf) + __cil_tmp60;
#line 173
    __cil_tmp62 = (char *)__cil_tmp61;
#line 173
    __cil_tmp63 = (char * __restrict  )__cil_tmp62;
#line 173
    __cil_tmp64 = (char const   * __restrict  )"GOOD";
#line 173
    strcpy(__cil_tmp63, __cil_tmp64);
#line 174
    __cil_tmp65 = (unsigned long )pw;
#line 174
    __cil_tmp66 = __cil_tmp65 + 32;
#line 174
    __cil_tmp67 = *((char **)__cil_tmp66);
#line 174
    __cil_tmp68 = (char const   *)__cil_tmp67;
#line 174
    tmp___2 = strcmp(__cil_tmp68, "/");
    }
#line 174
    if (tmp___2 == 0) {
#line 175
      __cil_tmp69 = (unsigned long )a;
#line 175
      __cil_tmp70 = __cil_tmp69 + 48;
#line 175
      *((char **)__cil_tmp70) = (char *)"";
    } else {
      {
#line 177
      __cil_tmp71 = (unsigned long )pw;
#line 177
      __cil_tmp72 = __cil_tmp71 + 32;
#line 177
      __cil_tmp73 = *((char **)__cil_tmp72);
#line 177
      __cil_tmp74 = (char const   *)__cil_tmp73;
#line 177
      tmp___0 = strlen(__cil_tmp74);
#line 177
      __cil_tmp75 = tmp___0 + 1UL;
#line 177
      __cil_tmp76 = (int )__cil_tmp75;
#line 177
      tmp___1 = xalloc(__cil_tmp76);
#line 177
      __cil_tmp77 = (unsigned long )a;
#line 177
      __cil_tmp78 = __cil_tmp77 + 48;
#line 177
      __cil_tmp79 = (char * __restrict  )tmp___1;
#line 177
      __cil_tmp80 = (unsigned long )pw;
#line 177
      __cil_tmp81 = __cil_tmp80 + 32;
#line 177
      __cil_tmp82 = *((char **)__cil_tmp81);
#line 177
      __cil_tmp83 = (char const   * __restrict  )__cil_tmp82;
#line 177
      *((char **)__cil_tmp78) = strcpy(__cil_tmp79, __cil_tmp83);
      }
    }
    {
#line 178
    __cil_tmp84 = (unsigned long )a;
#line 178
    __cil_tmp85 = __cil_tmp84 + 40;
#line 178
    __cil_tmp86 = (unsigned long )pw;
#line 178
    __cil_tmp87 = __cil_tmp86 + 16;
#line 178
    *((uid_t *)__cil_tmp85) = *((__uid_t *)__cil_tmp87);
#line 179
    __cil_tmp88 = (unsigned long )a;
#line 179
    __cil_tmp89 = __cil_tmp88 + 44;
#line 179
    __cil_tmp90 = (unsigned long )pw;
#line 179
    __cil_tmp91 = __cil_tmp90 + 20;
#line 179
    *((gid_t *)__cil_tmp89) = *((__gid_t *)__cil_tmp91);
#line 180
    __cil_tmp92 = *((char **)pw);
#line 180
    __cil_tmp93 = (char const   *)__cil_tmp92;
#line 180
    tmp___3 = strlen(__cil_tmp93);
#line 180
    __cil_tmp94 = tmp___3 + 1UL;
#line 180
    __cil_tmp95 = (int )__cil_tmp94;
#line 180
    tmp___4 = xalloc(__cil_tmp95);
#line 180
    __cil_tmp96 = (unsigned long )a;
#line 180
    __cil_tmp97 = __cil_tmp96 + 16;
#line 180
    __cil_tmp98 = (char * __restrict  )tmp___4;
#line 180
    __cil_tmp99 = *((char **)pw);
#line 180
    __cil_tmp100 = (char const   * __restrict  )__cil_tmp99;
#line 180
    *((char **)__cil_tmp97) = strcpy(__cil_tmp98, __cil_tmp100);
#line 181
    __cil_tmp101 = (unsigned long )a;
#line 181
    __cil_tmp102 = __cil_tmp101 + 32;
#line 181
    __cil_tmp103 = (unsigned long )a;
#line 181
    __cil_tmp104 = __cil_tmp103 + 32;
#line 181
    __cil_tmp105 = *((u_long *)__cil_tmp104);
#line 181
    *((u_long *)__cil_tmp102) = __cil_tmp105 | 4UL;
#line 183
    __cil_tmp106 = (char const   * __restrict  )"Before call to builfname, pw_gecos = %s, and pw_name = %s\n";
#line 183
    __cil_tmp107 = (unsigned long )pw;
#line 183
    __cil_tmp108 = __cil_tmp107 + 24;
#line 183
    __cil_tmp109 = *((char **)__cil_tmp108);
#line 183
    __cil_tmp110 = *((char **)pw);
#line 183
    printf(__cil_tmp106, __cil_tmp109, __cil_tmp110);
#line 184
    __cil_tmp111 = (char const   * __restrict  )"nbuf before call to buildfname = %s\n";
#line 184
    __cil_tmp112 = 0 * 1UL;
#line 184
    __cil_tmp113 = (unsigned long )(nbuf) + __cil_tmp112;
#line 184
    __cil_tmp114 = (char *)__cil_tmp113;
#line 184
    printf(__cil_tmp111, __cil_tmp114);
#line 186
    __cil_tmp115 = (unsigned long )pw;
#line 186
    __cil_tmp116 = __cil_tmp115 + 24;
#line 186
    __cil_tmp117 = *((char **)__cil_tmp116);
#line 186
    __cil_tmp118 = *((char **)pw);
#line 186
    __cil_tmp119 = 0 * 1UL;
#line 186
    __cil_tmp120 = (unsigned long )(nbuf) + __cil_tmp119;
#line 186
    __cil_tmp121 = (char *)__cil_tmp120;
#line 186
    buildfname(__cil_tmp117, __cil_tmp118, __cil_tmp121);
#line 187
    __cil_tmp122 = (char const   * __restrict  )"nbuf after call to buildfname = %s\n";
#line 187
    __cil_tmp123 = 0 * 1UL;
#line 187
    __cil_tmp124 = (unsigned long )(nbuf) + __cil_tmp123;
#line 187
    __cil_tmp125 = (char *)__cil_tmp124;
#line 187
    printf(__cil_tmp122, __cil_tmp125);
    }
    {
#line 189
    __cil_tmp126 = 0 * 1UL;
#line 189
    __cil_tmp127 = (unsigned long )(nbuf) + __cil_tmp126;
#line 189
    __cil_tmp128 = *((char *)__cil_tmp127);
#line 189
    __cil_tmp129 = (int )__cil_tmp128;
#line 189
    if (__cil_tmp129 != 0) {
      {
#line 190
      __cil_tmp130 = 0 * 1UL;
#line 190
      __cil_tmp131 = (unsigned long )(nbuf) + __cil_tmp130;
#line 190
      __cil_tmp132 = (char *)__cil_tmp131;
#line 190
      __cil_tmp133 = (char const   *)__cil_tmp132;
#line 190
      tmp___5 = strlen(__cil_tmp133);
#line 190
      __cil_tmp134 = tmp___5 + 1UL;
#line 190
      __cil_tmp135 = (int )__cil_tmp134;
#line 190
      tmp___6 = xalloc(__cil_tmp135);
#line 190
      __cil_tmp136 = (unsigned long )a;
#line 190
      __cil_tmp137 = __cil_tmp136 + 56;
#line 190
      __cil_tmp138 = (char * __restrict  )tmp___6;
#line 190
      __cil_tmp139 = 0 * 1UL;
#line 190
      __cil_tmp140 = (unsigned long )(nbuf) + __cil_tmp139;
#line 190
      __cil_tmp141 = (char *)__cil_tmp140;
#line 190
      __cil_tmp142 = (char const   * __restrict  )__cil_tmp141;
#line 190
      *((char **)__cil_tmp137) = strcpy(__cil_tmp138, __cil_tmp142);
      }
    } else {

    }
    }
    {
#line 192
    __cil_tmp143 = (char const   * __restrict  )"test_buf should be GOOD.  test_buf = %s\n";
#line 192
    __cil_tmp144 = 0 * 1UL;
#line 192
    __cil_tmp145 = (unsigned long )(test_buf) + __cil_tmp144;
#line 192
    __cil_tmp146 = (char *)__cil_tmp145;
#line 192
    printf(__cil_tmp143, __cil_tmp146);
    }
  }
  }
#line 197
  __cil_tmp147 = (unsigned long )a;
#line 197
  __cil_tmp148 = __cil_tmp147 + 32;
#line 197
  __cil_tmp149 = (unsigned long )a;
#line 197
  __cil_tmp150 = __cil_tmp149 + 32;
#line 197
  __cil_tmp151 = *((u_long *)__cil_tmp150);
#line 197
  *((u_long *)__cil_tmp148) = __cil_tmp151 | 0xffffffff80000000UL;
  {
#line 198
  __cil_tmp152 = 0 * 1UL;
#line 198
  __cil_tmp153 = (unsigned long )(buf0) + __cil_tmp152;
#line 198
  __cil_tmp154 = (char *)__cil_tmp153;
#line 198
  __cil_tmp155 = (unsigned long )__cil_tmp154;
#line 198
  __cil_tmp156 = (unsigned long )buf;
#line 198
  if (__cil_tmp156 != __cil_tmp155) {
    {
#line 199
    __cil_tmp157 = (void *)buf;
#line 199
    free(__cil_tmp157);
    }
  } else {

  }
  }
#line 202
  return (a);
}
}
#line 230 "recipient-bad.c"
struct passwd *finduser(char *name , enum bool *fuzzyp ) 
{ register struct passwd *pw ;
  register char *p ;
  enum bool tryagain ;
  int tmp ;
  unsigned short const   **tmp___0 ;
  char buf[5] ;
  char *tmp___1 ;
  int tmp___2 ;
  char const   *__cil_tmp11 ;
  void *__cil_tmp12 ;
  unsigned long __cil_tmp13 ;
  unsigned long __cil_tmp14 ;
  char __cil_tmp15 ;
  int __cil_tmp16 ;
  char __cil_tmp17 ;
  int __cil_tmp18 ;
  int __cil_tmp19 ;
  char __cil_tmp20 ;
  int __cil_tmp21 ;
  unsigned short const   *__cil_tmp22 ;
  unsigned short const   *__cil_tmp23 ;
  unsigned short __cil_tmp24 ;
  int __cil_tmp25 ;
  char __cil_tmp26 ;
  int __cil_tmp27 ;
  char const   *__cil_tmp28 ;
  void *__cil_tmp29 ;
  unsigned long __cil_tmp30 ;
  unsigned long __cil_tmp31 ;
  unsigned int __cil_tmp32 ;
  void *__cil_tmp33 ;
  char __cil_tmp34 ;
  int __cil_tmp35 ;
  int __cil_tmp36 ;
  int __cil_tmp37 ;
  char __cil_tmp38 ;
  int __cil_tmp39 ;
  char __cil_tmp40 ;
  int __cil_tmp41 ;
  void *__cil_tmp42 ;
  unsigned long __cil_tmp43 ;
  unsigned long __cil_tmp44 ;
  unsigned long __cil_tmp45 ;
  unsigned long __cil_tmp46 ;
  char *__cil_tmp47 ;
  char *__cil_tmp48 ;
  unsigned long __cil_tmp49 ;
  unsigned long __cil_tmp50 ;
  char *__cil_tmp51 ;
  unsigned long __cil_tmp52 ;
  unsigned long __cil_tmp53 ;
  char *__cil_tmp54 ;
  char const   *__cil_tmp55 ;
  void *__cil_tmp56 ;
  unsigned long __cil_tmp57 ;
  unsigned long __cil_tmp58 ;
  unsigned long __cil_tmp59 ;
  unsigned long __cil_tmp60 ;
  char *__cil_tmp61 ;
  char const   *__cil_tmp62 ;
  char const   *__cil_tmp63 ;
  void *__cil_tmp64 ;

  {
  {
#line 239
  *fuzzyp = (enum bool )0;
#line 253
  __cil_tmp11 = (char const   *)name;
#line 253
  pw = getpwnam(__cil_tmp11);
  }
  {
#line 255
  __cil_tmp12 = (void *)0;
#line 255
  __cil_tmp13 = (unsigned long )__cil_tmp12;
#line 255
  __cil_tmp14 = (unsigned long )pw;
#line 255
  if (__cil_tmp14 != __cil_tmp13) {
#line 257
    return (pw);
  } else {

  }
  }
#line 261
  tryagain = (enum bool )0;
#line 262
  p = name;
  {
#line 262
  while (1) {
    while_continue: /* CIL Label */ ;
    {
#line 262
    __cil_tmp15 = *p;
#line 262
    __cil_tmp16 = (int )__cil_tmp15;
#line 262
    if (__cil_tmp16 != 0) {

    } else {
#line 262
      goto while_break;
    }
    }
    {
#line 264
    __cil_tmp17 = *p;
#line 264
    __cil_tmp18 = (int )__cil_tmp17;
#line 264
    __cil_tmp19 = __cil_tmp18 & -128;
#line 264
    if (__cil_tmp19 == 0) {
      {
#line 264
      tmp___0 = __ctype_b_loc();
      }
      {
#line 264
      __cil_tmp20 = *p;
#line 264
      __cil_tmp21 = (int )__cil_tmp20;
#line 264
      __cil_tmp22 = *tmp___0;
#line 264
      __cil_tmp23 = __cil_tmp22 + __cil_tmp21;
#line 264
      __cil_tmp24 = *__cil_tmp23;
#line 264
      __cil_tmp25 = (int const   )__cil_tmp24;
#line 264
      if (__cil_tmp25 & 256) {
        {
#line 266
        __cil_tmp26 = *p;
#line 266
        __cil_tmp27 = (int )__cil_tmp26;
#line 266
        tmp = tolower(__cil_tmp27);
#line 266
        *p = (char )tmp;
#line 267
        tryagain = (enum bool )1;
        }
      } else {

      }
      }
    } else {

    }
    }
#line 262
    p = p + 1;
  }
  while_break: /* CIL Label */ ;
  }
  {
#line 270
  __cil_tmp28 = (char const   *)name;
#line 270
  pw = getpwnam(__cil_tmp28);
  }
#line 273
  if ((unsigned int )tryagain) {
    {
#line 273
    __cil_tmp29 = (void *)0;
#line 273
    __cil_tmp30 = (unsigned long )__cil_tmp29;
#line 273
    __cil_tmp31 = (unsigned long )pw;
#line 273
    if (__cil_tmp31 != __cil_tmp30) {
#line 275
      *fuzzyp = (enum bool )1;
#line 276
      return (pw);
    } else {

    }
    }
  } else {

  }
  {
#line 281
  __cil_tmp32 = (unsigned int )MatchGecos;
#line 281
  if (! __cil_tmp32) {
    {
#line 283
    __cil_tmp33 = (void *)0;
#line 283
    return ((struct passwd *)__cil_tmp33);
    }
  } else {

  }
  }
#line 287
  p = name;
  {
#line 287
  while (1) {
    while_continue___0: /* CIL Label */ ;
    {
#line 287
    __cil_tmp34 = *p;
#line 287
    __cil_tmp35 = (int )__cil_tmp34;
#line 287
    if (__cil_tmp35 != 0) {

    } else {
#line 287
      goto while_break___0;
    }
    }
    {
#line 289
    __cil_tmp36 = (int )SpaceSub;
#line 289
    __cil_tmp37 = __cil_tmp36 & 127;
#line 289
    __cil_tmp38 = *p;
#line 289
    __cil_tmp39 = (int )__cil_tmp38;
#line 289
    if (__cil_tmp39 == __cil_tmp37) {
#line 290
      *p = (char )' ';
    } else {
      {
#line 289
      __cil_tmp40 = *p;
#line 289
      __cil_tmp41 = (int )__cil_tmp40;
#line 289
      if (__cil_tmp41 == 95) {
#line 290
        *p = (char )' ';
      } else {

      }
      }
    }
    }
#line 287
    p = p + 1;
  }
  while_break___0: /* CIL Label */ ;
  }
  {
#line 292
  setpwent();
#line 293
  pw = getpwent();
  }
  {
#line 295
  while (1) {
    while_continue___1: /* CIL Label */ ;
    {
#line 295
    __cil_tmp42 = (void *)0;
#line 295
    __cil_tmp43 = (unsigned long )__cil_tmp42;
#line 295
    __cil_tmp44 = (unsigned long )pw;
#line 295
    if (__cil_tmp44 != __cil_tmp43) {

    } else {
#line 295
      goto while_break___1;
    }
    }
    {
#line 308
    __cil_tmp45 = (unsigned long )pw;
#line 308
    __cil_tmp46 = __cil_tmp45 + 24;
#line 308
    __cil_tmp47 = *((char **)__cil_tmp46);
#line 308
    __cil_tmp48 = *((char **)pw);
#line 308
    __cil_tmp49 = 0 * 1UL;
#line 308
    __cil_tmp50 = (unsigned long )(buf) + __cil_tmp49;
#line 308
    __cil_tmp51 = (char *)__cil_tmp50;
#line 308
    buildfname(__cil_tmp47, __cil_tmp48, __cil_tmp51);
#line 309
    __cil_tmp52 = 0 * 1UL;
#line 309
    __cil_tmp53 = (unsigned long )(buf) + __cil_tmp52;
#line 309
    __cil_tmp54 = (char *)__cil_tmp53;
#line 309
    __cil_tmp55 = (char const   *)__cil_tmp54;
#line 309
    tmp___1 = strchr(__cil_tmp55, ' ');
    }
    {
#line 309
    __cil_tmp56 = (void *)0;
#line 309
    __cil_tmp57 = (unsigned long )__cil_tmp56;
#line 309
    __cil_tmp58 = (unsigned long )tmp___1;
#line 309
    if (__cil_tmp58 != __cil_tmp57) {
      {
#line 309
      __cil_tmp59 = 0 * 1UL;
#line 309
      __cil_tmp60 = (unsigned long )(buf) + __cil_tmp59;
#line 309
      __cil_tmp61 = (char *)__cil_tmp60;
#line 309
      __cil_tmp62 = (char const   *)__cil_tmp61;
#line 309
      __cil_tmp63 = (char const   *)name;
#line 309
      tmp___2 = strcasecmp(__cil_tmp62, __cil_tmp63);
      }
#line 309
      if (tmp___2) {

      } else {
#line 311
        *fuzzyp = (enum bool )1;
#line 312
        return (pw);
      }
    } else {

    }
    }
    {
#line 315
    pw = getpwent();
    }
  }
  while_break___1: /* CIL Label */ ;
  }
  {
#line 320
  __cil_tmp64 = (void *)0;
#line 320
  return ((struct passwd *)__cil_tmp64);
  }
}
}
