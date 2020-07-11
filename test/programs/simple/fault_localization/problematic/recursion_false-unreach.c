// This file is part of CPAchecker,
// a tool for configurable software verification:
// https://cpachecker.sosy-lab.org
//
// SPDX-FileCopyrightText: 2007-2020 Dirk Beyer <https://www.sosy-lab.org>
//
// SPDX-License-Identifier: Apache-2.0

int main() {
	int x = 0;
	x++;
	if (x == 1) {
		int i = 0;
		int j = 2;
		while(i != j)
			i++;
		goto ERROR;
	}
	
EXIT: return 0;
ERROR: return 1;
}

