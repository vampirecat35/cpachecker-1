// This file is part of CPAchecker,
// a tool for configurable software verification:
// https://cpachecker.sosy-lab.org
//
// SPDX-FileCopyrightText: 2007-2020 Dirk Beyer <https://www.sosy-lab.org>
//
// SPDX-License-Identifier: Apache-2.0

extern int __VERIFIER_nondet_int();

int isErr(int x){
	if(x != -1 && x != 6) {
		return 1;
	}
	return 0;
}


int main(){
	int x = __VERIFIER_nondet_int();
	if(x > 0)
		if(x < 5)
			if(x > 1) {
				int t = isErr(x);			
				if(t) {
					goto ERROR;
				}
			}
EXIT: return 0;
ERROR: return 1;
}
