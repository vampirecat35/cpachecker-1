// This file is part of CPAchecker,
// a tool for configurable software verification:
// https://cpachecker.sosy-lab.org
//
// SPDX-FileCopyrightText: 2007-2020 Dirk Beyer <https://www.sosy-lab.org>
//
// SPDX-License-Identifier: Apache-2.0

void main() {
	int x;

L1:
	if (x) {
		goto L2;
	} else {
		goto L3;
	}

L2:
	if (x) {
		goto L3;
	} else {
		goto L4;
	}

L3:
	if (x) {
		goto L4;
	} else {
		goto L5;
	}

L4:
	if (x) {
		goto L5;
	} else {
		goto L1;
	}

L5:
	if (x) {
		goto L1;
	} else {
		goto L2;
	}

}
