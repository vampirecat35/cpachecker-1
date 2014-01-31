<?php

run();

function run() {
  $status = getStatus();

  for($i = 1; $i < 20; $i++) {
    $currentFile = 'Problem'.$i.'.c';

    $programSource = file_get_contents($currentFile);

    // replace some stuff that is not needed at all
    $preprocessedSource = str_replace('#include <stdio.h>', '', $programSource);
    $preprocessedSource = str_replace('#include <assert.h>', '', $preprocessedSource);
    $preprocessedSource = str_replace('#include <math.h>', '', $preprocessedSource);
    $preprocessedSource = str_replace(': assert(0);', ':;', $preprocessedSource);

    $preprocessedSource = 'extern int __VERIFIER_nondet_int();'.$preprocessedSource;
    $preprocessedSource = replaceMain($preprocessedSource);
  
    // default error labels (0 - 59)
    for($j = 0; $j < 60; $j++) {
      $finalProgramSource = setErrorLabel($j, $preprocessedSource);

      $state = 'unknown';
      if(isset($status[$i][$j])) {
        $state = $status[$i][$j];
      }

      file_put_contents(getNewFileName($i, $j, $state), $finalProgramSource);
    }

    // global error label
    $state = 'unknown';
    if(isset($status[$i][$j])) {
      $state = $status[$i][$j];
    }
    $finalProgramSource = str_replace('globalError:;', 'ERROR: goto ERROR;', $preprocessedSource);
    file_put_contents(getNewFileName($i, 60, $state), $finalProgramSource);
  }
}

function replaceMain($programSource) {

    $programSource = substr($programSource, 0, strpos($programSource, 'int main()'));

    $main = 'int main()'.PHP_EOL.
            '{'.PHP_EOL."\t".
                '// default output'.PHP_EOL."\t".
                'int output = -1;'.PHP_EOL.PHP_EOL."\t".

		'int bound = __VERIFIER_nondet_int();'.PHP_EOL."\t".
		'int i = 0;'.PHP_EOL.PHP_EOL."\t".

                '// main i/o-loop'.PHP_EOL."\t".
                'while(i < bound)'.PHP_EOL."\t".
                '{'.PHP_EOL."\t"."\t".
                    '// read input'.PHP_EOL."\t"."\t".
                    'int input = __VERIFIER_nondet_int();'.PHP_EOL."\t"."\t".
                    'if(input == 1) {'.PHP_EOL."\t"."\t".
                    '} else if(input == 2) {'.PHP_EOL."\t"."\t".
                    '} else if(input == 3) {'.PHP_EOL."\t"."\t".
                    '} else if(input == 4) {'.PHP_EOL."\t"."\t".
                    '} else if(input == 5) {'.PHP_EOL."\t"."\t".
                    '} else {'.PHP_EOL."\t"."\t"."\t".
                      'input = 6;'.PHP_EOL."\t"."\t".
                    '}'.PHP_EOL.PHP_EOL."\t"."\t".

                    '// operate eca engine'.PHP_EOL."\t"."\t".
                    'output = calculate_output(input);'.PHP_EOL.PHP_EOL."\t"."\t".
		    'i++;'.PHP_EOL."\t".
                '}'.PHP_EOL.
            '}'.PHP_EOL;

    return $programSource.$main;
}

function getNewFileName($i, $j, $state) {
  return 'Problem'.(str_pad($i, 2, '0', STR_PAD_LEFT)).'_'.(str_pad($j, 2, '0', STR_PAD_LEFT)).'_'.$state.'.c';
}

function setErrorLabel($errorCode, $programSource) {
    return str_replace('error_'.$errorCode.':;', 'ERROR: goto ERROR;', $programSource);
}

function getStatus() {
  $status[1][0] = 'safe';
  $status[1][1] = 'safe';
  $status[1][2] = 'safe';
  $status[1][3] = 'safe';
  $status[1][4] = 'safe';
  $status[1][5] = 'safe';
  $status[1][6] = 'safe';
  $status[1][7] = 'safe';
  $status[1][8] = 'safe';
  $status[1][9] = 'safe';
  $status[1][10] = 'safe';
  $status[1][11] = 'safe';
  $status[1][12] = 'safe';
  $status[1][13] = 'safe';
  $status[1][14] = 'safe';
  $status[1][15] = 'unsafe';
  $status[1][16] = 'safe';
  $status[1][17] = 'safe';
  $status[1][18] = 'safe';
  $status[1][19] = 'safe';
  $status[1][20] = 'unsafe';
  $status[1][21] = 'unsafe';
  $status[1][22] = 'safe';
  $status[1][23] = 'safe';
  $status[1][24] = 'safe';
  $status[1][25] = 'safe';
  $status[1][26] = 'safe';
  $status[1][27] = 'safe';
  $status[1][28] = 'safe';
  $status[1][29] = 'safe';
  $status[1][30] = 'safe';
  $status[1][31] = 'safe';
  $status[1][32] = 'unsafe';
  $status[1][33] = 'unsafe';
  $status[1][34] = 'safe';
  $status[1][35] = 'unsafe';
  $status[1][36] = 'safe';
  $status[1][37] = 'unsafe';
  $status[1][38] = 'unsafe';
  $status[1][39] = 'safe';
  $status[1][40] = 'safe';
  $status[1][41] = 'safe';
  $status[1][42] = 'safe';
  $status[1][43] = 'safe';
  $status[1][44] = 'unsafe';
  $status[1][45] = 'safe';
  $status[1][46] = 'safe';
  $status[1][47] = 'unsafe';
  $status[1][48] = 'safe';
  $status[1][49] = 'safe';
  $status[1][50] = 'unsafe';
  $status[1][51] = 'safe';
  $status[1][52] = 'safe';
  $status[1][53] = 'safe';
  $status[1][54] = 'safe';
  $status[1][55] = 'safe';
  $status[1][56] = 'unsafe';
  $status[1][57] = 'unsafe';
  $status[1][58] = 'safe';
  $status[1][59] = 'safe';
  $status[1][60] = 'unsafe';
  $status[2][0] = 'safe';
  $status[2][1] = 'safe';
  $status[2][2] = 'safe';
  $status[2][3] = 'safe';
  $status[2][4] = 'safe';
  $status[2][5] = 'safe';
  $status[2][6] = 'safe';
  $status[2][7] = 'safe';
  $status[2][8] = 'safe';
  $status[2][9] = 'safe';
  $status[2][10] = 'safe';
  $status[2][11] = 'safe';
  $status[2][12] = 'safe';
  $status[2][13] = 'unsafe';
  $status[2][14] = 'safe';
  $status[2][15] = 'safe';
  $status[2][16] = 'unsafe';
  $status[2][17] = 'safe';
  $status[2][18] = 'safe';
  $status[2][19] = 'safe';
  $status[2][20] = 'safe';
  $status[2][21] = 'safe';
  $status[2][22] = 'safe';
  $status[2][23] = 'safe';
  $status[2][24] = 'safe';
  $status[2][25] = 'safe';
  $status[2][26] = 'safe';
  $status[2][27] = 'safe';
  $status[2][28] = 'safe';
  $status[2][29] = 'safe';
  $status[2][30] = 'safe';
  $status[2][31] = 'safe';
  $status[2][32] = 'safe';
  $status[2][33] = 'safe';
  $status[2][34] = 'safe';
  $status[2][35] = 'safe';
  $status[2][36] = 'safe';
  $status[2][37] = 'safe';
  $status[2][38] = 'safe';
  $status[2][39] = 'safe';
  $status[2][40] = 'safe';
  $status[2][41] = 'safe';
  $status[2][42] = 'safe';
  $status[2][43] = 'unsafe';
  $status[2][44] = 'unsafe';
  $status[2][45] = 'unsafe';
  $status[2][46] = 'safe';
  $status[2][47] = 'safe';
  $status[2][48] = 'safe';
  $status[2][49] = 'safe';
  $status[2][50] = 'unsafe';
  $status[2][51] = 'safe';
  $status[2][52] = 'safe';
  $status[2][53] = 'safe';
  $status[2][54] = 'safe';
  $status[2][55] = 'safe';
  $status[2][56] = 'safe';
  $status[2][57] = 'safe';
  $status[2][58] = 'safe';
  $status[2][59] = 'unsafe';
  $status[2][60] = 'unsafe';
  $status[3][0] = 'safe';
  $status[3][1] = 'safe';
  $status[3][2] = 'safe';
  $status[3][3] = 'safe';
  $status[3][4] = 'safe';
  $status[3][5] = 'safe';
  $status[3][6] = 'safe';
  $status[3][7] = 'safe';
  $status[3][8] = 'safe';
  $status[3][9] = 'unsafe';
  $status[3][10] = 'safe';
  $status[3][11] = 'safe';
  $status[3][12] = 'safe';
  $status[3][13] = 'unsafe';
  $status[3][14] = 'safe';
  $status[3][15] = 'safe';
  $status[3][16] = 'safe';
  $status[3][17] = 'safe';
  $status[3][18] = 'safe';
  $status[3][19] = 'safe';
  $status[3][20] = 'safe';
  $status[3][21] = 'safe';
  $status[3][22] = 'safe';
  $status[3][23] = 'safe';
  $status[3][24] = 'safe';
  $status[3][25] = 'safe';
  $status[3][26] = 'unsafe';
  $status[3][27] = 'unsafe';
  $status[3][28] = 'unsafe';
  $status[3][29] = 'safe';
  $status[3][30] = 'safe';
  $status[3][31] = 'unsafe';
  $status[3][32] = 'safe';
  $status[3][33] = 'safe';
  $status[3][34] = 'safe';
  $status[3][35] = 'unsafe';
  $status[3][36] = 'safe';
  $status[3][37] = 'unsafe';
  $status[3][38] = 'safe';
  $status[3][39] = 'unsafe';
  $status[3][40] = 'safe';
  $status[3][41] = 'safe';
  $status[3][42] = 'safe';
  $status[3][43] = 'unsafe';
  $status[3][44] = 'safe';
  $status[3][45] = 'unsafe';
  $status[3][46] = 'safe';
  $status[3][47] = 'safe';
  $status[3][48] = 'safe';
  $status[3][49] = 'safe';
  $status[3][50] = 'unsafe';
  $status[3][51] = 'safe';
  $status[3][52] = 'unsafe';
  $status[3][53] = 'safe';
  $status[3][54] = 'safe';
  $status[3][55] = 'safe';
  $status[3][56] = 'safe';
  $status[3][57] = 'safe';
  $status[3][58] = 'safe';
  $status[3][59] = 'safe';
  $status[3][60] = 'unsafe';
  $status[4][0] = 'safe';
  $status[4][1] = 'safe';
  $status[4][2] = 'safe';
  $status[4][3] = 'safe';
  $status[4][4] = 'unsafe';
  $status[4][5] = 'safe';
  $status[4][6] = 'unsafe';
  $status[4][7] = 'safe';
  $status[4][8] = 'safe';
  $status[4][9] = 'unsafe';
  $status[4][10] = 'safe';
  $status[4][11] = 'unsafe';
  $status[4][12] = 'unsafe';
  $status[4][13] = 'unsafe';
  $status[4][14] = 'unsafe';
  $status[4][15] = 'unsafe';
  $status[4][16] = 'safe';
  $status[4][17] = 'unsafe';
  $status[4][18] = 'unsafe';
  $status[4][19] = 'unsafe';
  $status[4][20] = 'safe';
  $status[4][21] = 'safe';
  $status[4][22] = 'safe';
  $status[4][23] = 'safe';
  $status[4][24] = 'safe';
  $status[4][25] = 'safe';
  $status[4][26] = 'unsafe';
  $status[4][27] = 'unsafe';
  $status[4][28] = 'safe';
  $status[4][29] = 'safe';
  $status[4][30] = 'safe';
  $status[4][31] = 'unsafe';
  $status[4][32] = 'unsafe';
  $status[4][33] = 'safe';
  $status[4][34] = 'safe';
  $status[4][35] = 'unsafe';
  $status[4][36] = 'unsafe';
  $status[4][37] = 'safe';
  $status[4][38] = 'unsafe';
  $status[4][39] = 'unsafe';
  $status[4][40] = 'unsafe';
  $status[4][41] = 'safe';
  $status[4][42] = 'safe';
  $status[4][43] = 'safe';
  $status[4][44] = 'safe';
  $status[4][45] = 'unsafe';
  $status[4][46] = 'safe';
  $status[4][47] = 'safe';
  $status[4][48] = 'safe';
  $status[4][49] = 'safe';
  $status[4][50] = 'safe';
  $status[4][51] = 'safe';
  $status[4][52] = 'unsafe';
  $status[4][53] = 'safe';
  $status[4][54] = 'safe';
  $status[4][55] = 'unsafe';
  $status[4][56] = 'safe';
  $status[4][57] = 'safe';
  $status[4][58] = 'unsafe';
  $status[4][59] = 'safe';
  $status[4][60] = 'unsafe';
  $status[5][0] = 'unsafe';
  $status[5][1] = 'unsafe';
  $status[5][2] = 'safe';
  $status[5][3] = 'safe';
  $status[5][4] = 'safe';
  $status[5][5] = 'safe';
  $status[5][6] = 'safe';
  $status[5][7] = 'safe';
  $status[5][8] = 'safe';
  $status[5][9] = 'safe';
  $status[5][10] = 'safe';
  $status[5][11] = 'unsafe';
  $status[5][12] = 'safe';
  $status[5][13] = 'unsafe';
  $status[5][14] = 'safe';
  $status[5][15] = 'unsafe';
  $status[5][16] = 'safe';
  $status[5][17] = 'safe';
  $status[5][18] = 'unsafe';
  $status[5][19] = 'safe';
  $status[5][20] = 'safe';
  $status[5][21] = 'safe';
  $status[5][22] = 'safe';
  $status[5][23] = 'safe';
  $status[5][24] = 'unsafe';
  $status[5][25] = 'safe';
  $status[5][26] = 'unsafe';
  $status[5][27] = 'safe';
  $status[5][28] = 'safe';
  $status[5][29] = 'safe';
  $status[5][30] = 'unsafe';
  $status[5][31] = 'safe';
  $status[5][32] = 'unsafe';
  $status[5][33] = 'unsafe';
  $status[5][34] = 'safe';
  $status[5][35] = 'safe';
  $status[5][36] = 'unsafe';
  $status[5][37] = 'unsafe';
  $status[5][38] = 'unsafe';
  $status[5][39] = 'unsafe';
  $status[5][40] = 'unsafe';
  $status[5][41] = 'unsafe';
  $status[5][42] = 'safe';
  $status[5][43] = 'safe';
  $status[5][44] = 'unsafe';
  $status[5][45] = 'safe';
  $status[5][46] = 'safe';
  $status[5][47] = 'unsafe';
  $status[5][48] = 'unsafe';
  $status[5][49] = 'safe';
  $status[5][50] = 'safe';
  $status[5][51] = 'unsafe';
  $status[5][52] = 'safe';
  $status[5][53] = 'safe';
  $status[5][54] = 'safe';
  $status[5][55] = 'unsafe';
  $status[5][56] = 'safe';
  $status[5][57] = 'unsafe';
  $status[5][58] = 'unsafe';
  $status[5][59] = 'safe';
  $status[5][60] = 'unsafe';
  $status[6][0] = 'unsafe';
  $status[6][1] = 'unsafe';
  $status[6][2] = 'unsafe';
  $status[6][3] = 'safe';
  $status[6][4] = 'unsafe';
  $status[6][5] = 'unsafe';
  $status[6][6] = 'safe';
  $status[6][7] = 'safe';
  $status[6][8] = 'safe';
  $status[6][9] = 'unsafe';
  $status[6][10] = 'unsafe';
  $status[6][11] = 'unsafe';
  $status[6][12] = 'unsafe';
  $status[6][13] = 'safe';
  $status[6][14] = 'safe';
  $status[6][15] = 'unsafe';
  $status[6][16] = 'safe';
  $status[6][17] = 'safe';
  $status[6][18] = 'safe';
  $status[6][19] = 'safe';
  $status[6][20] = 'unsafe';
  $status[6][21] = 'unsafe';
  $status[6][22] = 'safe';
  $status[6][23] = 'safe';
  $status[6][24] = 'unsafe';
  $status[6][25] = 'safe';
  $status[6][26] = 'safe';
  $status[6][27] = 'unsafe';
  $status[6][28] = 'safe';
  $status[6][29] = 'unsafe';
  $status[6][30] = 'safe';
  $status[6][31] = 'safe';
  $status[6][32] = 'safe';
  $status[6][33] = 'unsafe';
  $status[6][34] = 'safe';
  $status[6][35] = 'safe';
  $status[6][36] = 'unsafe';
  $status[6][37] = 'unsafe';
  $status[6][38] = 'unsafe';
  $status[6][39] = 'safe';
  $status[6][40] = 'safe';
  $status[6][41] = 'safe';
  $status[6][42] = 'safe';
  $status[6][43] = 'safe';
  $status[6][44] = 'unsafe';
  $status[6][45] = 'safe';
  $status[6][46] = 'safe';
  $status[6][47] = 'unsafe';
  $status[6][48] = 'unsafe';
  $status[6][49] = 'safe';
  $status[6][50] = 'safe';
  $status[6][51] = 'safe';
  $status[6][52] = 'safe';
  $status[6][53] = 'safe';
  $status[6][54] = 'safe';
  $status[6][55] = 'safe';
  $status[6][56] = 'unsafe';
  $status[6][57] = 'safe';
  $status[6][58] = 'unsafe';
  $status[6][59] = 'unsafe';
  $status[6][60] = 'unsafe';
  $status[7][0] = 'safe';
  $status[7][1] = 'safe';
  $status[7][2] = 'safe';
  $status[7][3] = 'unsafe';
  $status[7][4] = 'safe';
  $status[7][5] = 'unsafe';
  $status[7][6] = 'unsafe';
  $status[7][7] = 'unsafe';
  $status[7][8] = 'safe';
  $status[7][9] = 'unsafe';
  $status[7][10] = 'safe';
  $status[7][11] = 'unsafe';
  $status[7][12] = 'safe';
  $status[7][13] = 'safe';
  $status[7][14] = 'safe';
  $status[7][15] = 'unsafe';
  $status[7][16] = 'safe';
  $status[7][17] = 'safe';
  $status[7][18] = 'unsafe';
  $status[7][19] = 'unsafe';
  $status[7][20] = 'unsafe';
  $status[7][21] = 'safe';
  $status[7][22] = 'safe';
  $status[7][23] = 'unsafe';
  $status[7][24] = 'safe';
  $status[7][25] = 'safe';
  $status[7][26] = 'safe';
  $status[7][27] = 'safe';
  $status[7][28] = 'safe';
  $status[7][29] = 'safe';
  $status[7][30] = 'unsafe';
  $status[7][31] = 'unsafe';
  $status[7][32] = 'safe';
  $status[7][33] = 'safe';
  $status[7][34] = 'safe';
  $status[7][35] = 'unsafe';
  $status[7][36] = 'unsafe';
  $status[7][37] = 'unsafe';
  $status[7][38] = 'safe';
  $status[7][39] = 'unsafe';
  $status[7][40] = 'unsafe';
  $status[7][41] = 'safe';
  $status[7][42] = 'unsafe';
  $status[7][43] = 'safe';
  $status[7][44] = 'unsafe';
  $status[7][45] = 'safe';
  $status[7][46] = 'unsafe';
  $status[7][47] = 'unsafe';
  $status[7][48] = 'unsafe';
  $status[7][49] = 'safe';
  $status[7][50] = 'safe';
  $status[7][51] = 'safe';
  $status[7][52] = 'safe';
  $status[7][53] = 'safe';
  $status[7][54] = 'safe';
  $status[7][55] = 'safe';
  $status[7][56] = 'safe';
  $status[7][57] = 'safe';
  $status[7][58] = 'unsafe';
  $status[7][59] = 'safe';
  $status[7][60] = 'unsafe';
  $status[8][0] = 'safe';
  $status[8][1] = 'unsafe';
  $status[8][2] = 'unsafe';
  $status[8][3] = 'safe';
  $status[8][4] = 'unsafe';
  $status[8][5] = 'unsafe';
  $status[8][6] = 'unsafe';
  $status[8][7] = 'unsafe';
  $status[8][8] = 'safe';
  $status[8][9] = 'safe';
  $status[8][10] = 'unsafe';
  $status[8][11] = 'safe';
  $status[8][12] = 'safe';
  $status[8][13] = 'unsafe';
  $status[8][14] = 'safe';
  $status[8][15] = 'unsafe';
  $status[8][16] = 'safe';
  $status[8][17] = 'safe';
  $status[8][18] = 'safe';
  $status[8][19] = 'safe';
  $status[8][20] = 'safe';
  $status[8][21] = 'safe';
  $status[8][22] = 'safe';
  $status[8][23] = 'safe';
  $status[8][24] = 'unsafe';
  $status[8][25] = 'unsafe';
  $status[8][26] = 'unsafe';
  $status[8][27] = 'safe';
  $status[8][28] = 'unsafe';
  $status[8][29] = 'unsafe';
  $status[8][30] = 'safe';
  $status[8][31] = 'safe';
  $status[8][32] = 'safe';
  $status[8][33] = 'safe';
  $status[8][34] = 'unsafe';
  $status[8][35] = 'safe';
  $status[8][36] = 'safe';
  $status[8][37] = 'unsafe';
  $status[8][38] = 'safe';
  $status[8][39] = 'safe';
  $status[8][40] = 'safe';
  $status[8][41] = 'safe';
  $status[8][42] = 'safe';
  $status[8][43] = 'unsafe';
  $status[8][44] = 'safe';
  $status[8][45] = 'safe';
  $status[8][46] = 'unsafe';
  $status[8][47] = 'safe';
  $status[8][48] = 'unsafe';
  $status[8][49] = 'unsafe';
  $status[8][50] = 'unsafe';
  $status[8][51] = 'unsafe';
  $status[8][52] = 'safe';
  $status[8][53] = 'safe';
  $status[8][54] = 'safe';
  $status[8][55] = 'unsafe';
  $status[8][56] = 'safe';
  $status[8][57] = 'safe';
  $status[8][58] = 'safe';
  $status[8][59] = 'unsafe';
  $status[8][60] = 'unsafe';
  $status[9][0] = 'safe';
  $status[9][1] = 'safe';
  $status[9][2] = 'unsafe';
  $status[9][3] = 'unsafe';
  $status[9][4] = 'safe';
  $status[9][5] = 'safe';
  $status[9][6] = 'unsafe';
  $status[9][7] = 'safe';
  $status[9][8] = 'unsafe';
  $status[9][9] = 'safe';
  $status[9][10] = 'unsafe';
  $status[9][11] = 'unsafe';
  $status[9][12] = 'safe';
  $status[9][13] = 'safe';
  $status[9][14] = 'safe';
  $status[9][15] = 'unsafe';
  $status[9][16] = 'safe';
  $status[9][17] = 'safe';
  $status[9][18] = 'safe';
  $status[9][19] = 'unsafe';
  $status[9][20] = 'unsafe';
  $status[9][21] = 'safe';
  $status[9][22] = 'safe';
  $status[9][23] = 'safe';
  $status[9][24] = 'safe';
  $status[9][25] = 'safe';
  $status[9][26] = 'safe';
  $status[9][27] = 'safe';
  $status[9][28] = 'safe';
  $status[9][29] = 'safe';
  $status[9][30] = 'safe';
  $status[9][31] = 'safe';
  $status[9][32] = 'unsafe';
  $status[9][33] = 'safe';
  $status[9][34] = 'unsafe';
  $status[9][35] = 'unsafe';
  $status[9][36] = 'unsafe';
  $status[9][37] = 'safe';
  $status[9][38] = 'unsafe';
  $status[9][39] = 'safe';
  $status[9][40] = 'safe';
  $status[9][41] = 'unsafe';
  $status[9][42] = 'safe';
  $status[9][43] = 'safe';
  $status[9][44] = 'unsafe';
  $status[9][45] = 'safe';
  $status[9][46] = 'unsafe';
  $status[9][47] = 'unsafe';
  $status[9][48] = 'safe';
  $status[9][49] = 'safe';
  $status[9][50] = 'safe';
  $status[9][51] = 'unsafe';
  $status[9][52] = 'safe';
  $status[9][53] = 'unsafe';
  $status[9][54] = 'unsafe';
  $status[9][55] = 'safe';
  $status[9][56] = 'unsafe';
  $status[9][57] = 'unsafe';
  $status[9][58] = 'safe';
  $status[9][59] = 'unsafe';
  $status[9][60] = 'unsafe';
  $status[10][0] = 'safe';
  $status[10][1] = 'safe';
  $status[10][2] = 'safe';
  $status[10][3] = 'safe';
  $status[10][4] = 'safe';
  $status[10][5] = 'safe';
  $status[10][6] = 'safe';
  $status[10][7] = 'safe';
  $status[10][8] = 'safe';
  $status[10][9] = 'safe';
  $status[10][10] = 'safe';
  $status[10][11] = 'unsafe';
  $status[10][12] = 'unsafe';
  $status[10][13] = 'unsafe';
  $status[10][14] = 'unsafe';
  $status[10][15] = 'unsafe';
  $status[10][16] = 'safe';
  $status[10][17] = 'safe';
  $status[10][18] = 'safe';
  $status[10][19] = 'safe';
  $status[10][20] = 'safe';
  $status[10][21] = 'safe';
  $status[10][22] = 'safe';
  $status[10][23] = 'unsafe';
  $status[10][24] = 'unsafe';
  $status[10][25] = 'unsafe';
  $status[10][26] = 'unsafe';
  $status[10][27] = 'unsafe';
  $status[10][28] = 'unsafe';
  $status[10][29] = 'unsafe';
  $status[10][30] = 'unsafe';
  $status[10][31] = 'safe';
  $status[10][32] = 'safe';
  $status[10][33] = 'safe';
  $status[10][34] = 'safe';
  $status[10][35] = 'safe';
  $status[10][36] = 'safe';
  $status[10][37] = 'safe';
  $status[10][38] = 'safe';
  $status[10][39] = 'unsafe';
  $status[10][40] = 'unsafe';
  $status[10][41] = 'unsafe';
  $status[10][42] = 'unsafe';
  $status[10][43] = 'safe';
  $status[10][44] = 'safe';
  $status[10][45] = 'safe';
  $status[10][46] = 'unsafe';
  $status[10][47] = 'unsafe';
  $status[10][48] = 'unsafe';
  $status[10][49] = 'unknown';
  $status[10][50] = 'unsafe';
  $status[10][51] = 'safe';
  $status[10][52] = 'safe';
  $status[10][53] = 'safe';
  $status[10][54] = 'safe';
  $status[10][55] = 'unsafe';
  $status[10][56] = 'unsafe';
  $status[10][57] = 'unsafe';
  $status[10][58] = 'unsafe';
  $status[10][59] = 'unsafe';
  $status[10][60] = 'unsafe';
  $status[11][0] = 'unsafe';
  $status[11][1] = 'unsafe';
  $status[11][2] = 'unsafe';
  $status[11][3] = 'unsafe';
  $status[11][4] = 'unsafe';
  $status[11][5] = 'unsafe';
  $status[11][6] = 'unsafe';
  $status[11][7] = 'unsafe';
  $status[11][8] = 'unsafe';
  $status[11][9] = 'unsafe';
  $status[11][10] = 'unsafe';
  $status[11][11] = 'unsafe';
  $status[11][12] = 'unsafe';
  $status[11][13] = 'unsafe';
  $status[11][14] = 'unsafe';
  $status[11][15] = 'unsafe';
  $status[11][16] = 'unsafe';
  $status[11][17] = 'unsafe';
  $status[11][18] = 'unsafe';
  $status[11][19] = 'unsafe';
  $status[11][20] = 'unsafe';
  $status[11][21] = 'unsafe';
  $status[11][22] = 'unsafe';
  $status[11][23] = 'unsafe';
  $status[11][24] = 'unsafe';
  $status[11][25] = 'unsafe';
  $status[11][26] = 'unsafe';
  $status[11][27] = 'unsafe';
  $status[11][28] = 'unsafe';
  $status[11][29] = 'unsafe';
  $status[11][30] = 'unsafe';
  $status[11][31] = 'unsafe';
  $status[11][32] = 'unsafe';
  $status[11][33] = 'unsafe';
  $status[11][34] = 'unsafe';
  $status[11][35] = 'unsafe';
  $status[11][36] = 'unsafe';
  $status[11][37] = 'unsafe';
  $status[11][38] = 'unsafe';
  $status[11][39] = 'unsafe';
  $status[11][40] = 'unsafe';
  $status[11][41] = 'unsafe';
  $status[11][42] = 'unsafe';
  $status[11][43] = 'unsafe';
  $status[11][44] = 'unsafe';
  $status[11][45] = 'unsafe';
  $status[11][46] = 'unsafe';
  $status[11][47] = 'unsafe';
  $status[11][48] = 'unsafe';
  $status[11][49] = 'unsafe';
  $status[11][50] = 'unsafe';
  $status[11][51] = 'unsafe';
  $status[11][52] = 'unsafe';
  $status[11][53] = 'unsafe';
  $status[11][54] = 'unsafe';
  $status[11][55] = 'unsafe';
  $status[11][56] = 'unsafe';
  $status[11][57] = 'unsafe';
  $status[11][58] = 'unsafe';
  $status[11][59] = 'unsafe';
  $status[11][60] = 'unsafe';
  $status[12][0] = 'unsafe';
  $status[12][1] = 'unsafe';
  $status[12][2] = 'unsafe';
  $status[12][3] = 'unsafe';
  $status[12][4] = 'unsafe';
  $status[12][5] = 'unsafe';
  $status[12][6] = 'unsafe';
  $status[12][7] = 'unsafe';
  $status[12][8] = 'unsafe';
  $status[12][9] = 'unsafe';
  $status[12][10] = 'unsafe';
  $status[12][11] = 'unsafe';
  $status[12][12] = 'unsafe';
  $status[12][13] = 'unsafe';
  $status[12][14] = 'unsafe';
  $status[12][15] = 'unsafe';
  $status[12][16] = 'unsafe';
  $status[12][17] = 'unsafe';
  $status[12][18] = 'unsafe';
  $status[12][19] = 'unsafe';
  $status[12][20] = 'unsafe';
  $status[12][21] = 'unsafe';
  $status[12][22] = 'unsafe';
  $status[12][23] = 'unsafe';
  $status[12][24] = 'unsafe';
  $status[12][25] = 'unsafe';
  $status[12][26] = 'unsafe';
  $status[12][27] = 'unsafe';
  $status[12][28] = 'unsafe';
  $status[12][29] = 'unsafe';
  $status[12][30] = 'unsafe';
  $status[12][31] = 'unsafe';
  $status[12][32] = 'unsafe';
  $status[12][33] = 'unsafe';
  $status[12][34] = 'unsafe';
  $status[12][35] = 'unsafe';
  $status[12][36] = 'unsafe';
  $status[12][37] = 'unsafe';
  $status[12][38] = 'unsafe';
  $status[12][39] = 'unsafe';
  $status[12][40] = 'unsafe';
  $status[12][41] = 'unsafe';
  $status[12][42] = 'unsafe';
  $status[12][43] = 'unsafe';
  $status[12][44] = 'unsafe';
  $status[12][45] = 'unsafe';
  $status[12][46] = 'unsafe';
  $status[12][47] = 'unsafe';
  $status[12][48] = 'unsafe';
  $status[12][49] = 'unsafe';
  $status[12][50] = 'unsafe';
  $status[12][51] = 'unsafe';
  $status[12][52] = 'unsafe';
  $status[12][53] = 'unsafe';
  $status[12][54] = 'unsafe';
  $status[12][55] = 'unsafe';
  $status[12][56] = 'unsafe';
  $status[12][57] = 'unsafe';
  $status[12][58] = 'unsafe';
  $status[12][59] = 'unsafe';
  $status[12][60] = 'unsafe';
  $status[13][0] = 'unsafe';
  $status[13][1] = 'unsafe';
  $status[13][2] = 'unsafe';
  $status[13][3] = 'unsafe';
  $status[13][4] = 'unsafe';
  $status[13][5] = 'unsafe';
  $status[13][6] = 'unsafe';
  $status[13][7] = 'unsafe';
  $status[13][8] = 'unsafe';
  $status[13][9] = 'unsafe';
  $status[13][10] = 'unsafe';
  $status[13][11] = 'unsafe';
  $status[13][12] = 'unsafe';
  $status[13][13] = 'unsafe';
  $status[13][14] = 'unsafe';
  $status[13][15] = 'unsafe';
  $status[13][16] = 'unsafe';
  $status[13][17] = 'unsafe';
  $status[13][18] = 'unsafe';
  $status[13][19] = 'unsafe';
  $status[13][20] = 'unsafe';
  $status[13][21] = 'unsafe';
  $status[13][22] = 'unsafe';
  $status[13][23] = 'unsafe';
  $status[13][24] = 'unsafe';
  $status[13][25] = 'unsafe';
  $status[13][26] = 'unknown';
  $status[13][27] = 'unsafe';
  $status[13][28] = 'unsafe';
  $status[13][29] = 'unsafe';
  $status[13][30] = 'unsafe';
  $status[13][31] = 'unsafe';
  $status[13][32] = 'unsafe';
  $status[13][33] = 'unsafe';
  $status[13][34] = 'unsafe';
  $status[13][35] = 'unsafe';
  $status[13][36] = 'unsafe';
  $status[13][37] = 'unsafe';
  $status[13][38] = 'unsafe';
  $status[13][39] = 'unsafe';
  $status[13][40] = 'unsafe';
  $status[13][41] = 'unsafe';
  $status[13][42] = 'unsafe';
  $status[13][43] = 'unsafe';
  $status[13][44] = 'unsafe';
  $status[13][45] = 'unsafe';
  $status[13][46] = 'unsafe';
  $status[13][47] = 'unsafe';
  $status[13][48] = 'unsafe';
  $status[13][49] = 'unsafe';
  $status[13][50] = 'unsafe';
  $status[13][51] = 'unsafe';
  $status[13][52] = 'unknown';
  $status[13][53] = 'unsafe';
  $status[13][54] = 'unsafe';
  $status[13][55] = 'unsafe';
  $status[13][56] = 'unsafe';
  $status[13][57] = 'unsafe';
  $status[13][58] = 'unsafe';
  $status[13][59] = 'unsafe';
  $status[13][60] = 'unsafe';
  $status[14][0] = 'safe';
  $status[14][1] = 'safe';
  $status[14][2] = 'unsafe';
  $status[14][3] = 'unsafe';
  $status[14][4] = 'unsafe';
  $status[14][5] = 'safe';
  $status[14][6] = 'safe';
  $status[14][7] = 'unsafe';
  $status[14][8] = 'unsafe';
  $status[14][9] = 'unsafe';
  $status[14][10] = 'unsafe';
  $status[14][11] = 'unsafe';
  $status[14][12] = 'unsafe';
  $status[14][13] = 'unsafe';
  $status[14][14] = 'unsafe';
  $status[14][15] = 'safe';
  $status[14][16] = 'safe';
  $status[14][17] = 'unsafe';
  $status[14][18] = 'unsafe';
  $status[14][19] = 'unsafe';
  $status[14][20] = 'safe';
  $status[14][21] = 'safe';
  $status[14][22] = 'unsafe';
  $status[14][23] = 'unsafe';
  $status[14][24] = 'unsafe';
  $status[14][25] = 'safe';
  $status[14][26] = 'safe';
  $status[14][27] = 'unsafe';
  $status[14][28] = 'unsafe';
  $status[14][29] = 'unsafe';
  $status[14][30] = 'safe';
  $status[14][31] = 'unsafe';
  $status[14][32] = 'unsafe';
  $status[14][33] = 'unsafe';
  $status[14][34] = 'unsafe';
  $status[14][35] = 'safe';
  $status[14][36] = 'safe';
  $status[14][37] = 'unsafe';
  $status[14][38] = 'unsafe';
  $status[14][39] = 'unsafe';
  $status[14][40] = 'unsafe';
  $status[14][41] = 'unsafe';
  $status[14][42] = 'safe';
  $status[14][43] = 'unsafe';
  $status[14][44] = 'unsafe';
  $status[14][45] = 'safe';
  $status[14][46] = 'unsafe';
  $status[14][47] = 'safe';
  $status[14][48] = 'unsafe';
  $status[14][49] = 'unsafe';
  $status[14][50] = 'safe';
  $status[14][51] = 'unsafe';
  $status[14][52] = 'unsafe';
  $status[14][53] = 'unsafe';
  $status[14][54] = 'unsafe';
  $status[14][55] = 'safe';
  $status[14][56] = 'unsafe';
  $status[14][57] = 'unsafe';
  $status[14][58] = 'unsafe';
  $status[14][59] = 'safe';
  $status[14][60] = 'unsafe';
  $status[15][0] = 'unsafe';
  $status[15][1] = 'unsafe';
  $status[15][2] = 'unsafe';
  $status[15][3] = 'unsafe';
  $status[15][4] = 'unsafe';
  $status[15][5] = 'unsafe';
  $status[15][6] = 'unsafe';
  $status[15][7] = 'unsafe';
  $status[15][8] = 'unsafe';
  $status[15][9] = 'unsafe';
  $status[15][10] = 'unsafe';
  $status[15][11] = 'unsafe';
  $status[15][12] = 'unsafe';
  $status[15][13] = 'unsafe';
  $status[15][14] = 'unsafe';
  $status[15][15] = 'unsafe';
  $status[15][16] = 'unsafe';
  $status[15][17] = 'unsafe';
  $status[15][18] = 'unsafe';
  $status[15][19] = 'unsafe';
  $status[15][20] = 'unsafe';
  $status[15][21] = 'unsafe';
  $status[15][22] = 'unsafe';
  $status[15][23] = 'unsafe';
  $status[15][24] = 'unsafe';
  $status[15][25] = 'unsafe';
  $status[15][26] = 'unsafe';
  $status[15][27] = 'unsafe';
  $status[15][28] = 'unsafe';
  $status[15][29] = 'unsafe';
  $status[15][30] = 'unsafe';
  $status[15][31] = 'unsafe';
  $status[15][32] = 'unsafe';
  $status[15][33] = 'unsafe';
  $status[15][34] = 'unsafe';
  $status[15][35] = 'unknown';
  $status[15][36] = 'unsafe';
  $status[15][37] = 'unsafe';
  $status[15][38] = 'unsafe';
  $status[15][39] = 'unsafe';
  $status[15][40] = 'unsafe';
  $status[15][41] = 'unsafe';
  $status[15][42] = 'unsafe';
  $status[15][43] = 'unsafe';
  $status[15][44] = 'unsafe';
  $status[15][45] = 'unsafe';
  $status[15][46] = 'unsafe';
  $status[15][47] = 'unsafe';
  $status[15][48] = 'unsafe';
  $status[15][49] = 'unsafe';
  $status[15][50] = 'unsafe';
  $status[15][51] = 'unsafe';
  $status[15][52] = 'unsafe';
  $status[15][53] = 'unsafe';
  $status[15][54] = 'unsafe';
  $status[15][55] = 'unsafe';
  $status[15][56] = 'unsafe';
  $status[15][57] = 'unsafe';
  $status[15][58] = 'unsafe';
  $status[15][59] = 'unsafe';
  $status[15][60] = 'unsafe';
  $status[16][0] = 'unsafe';
  $status[16][1] = 'unsafe';
  $status[16][2] = 'unsafe';
  $status[16][3] = 'unsafe';
  $status[16][4] = 'unsafe';
  $status[16][5] = 'unsafe';
  $status[16][6] = 'unsafe';
  $status[16][7] = 'unknown';
  $status[16][8] = 'unsafe';
  $status[16][9] = 'unsafe';
  $status[16][10] = 'unknown';
  $status[16][11] = 'safe';
  $status[16][12] = 'safe';
  $status[16][13] = 'safe';
  $status[16][14] = 'unsafe';
  $status[16][15] = 'unsafe';
  $status[16][16] = 'safe';
  $status[16][17] = 'safe';
  $status[16][18] = 'unsafe';
  $status[16][19] = 'unsafe';
  $status[16][20] = 'unsafe';
  $status[16][21] = 'unknown';
  $status[16][22] = 'unsafe';
  $status[16][23] = 'safe';
  $status[16][24] = 'safe';
  $status[16][25] = 'safe';
  $status[16][26] = 'safe';
  $status[16][27] = 'unsafe';
  $status[16][28] = 'unsafe';
  $status[16][29] = 'unsafe';
  $status[16][30] = 'unsafe';
  $status[16][31] = 'unsafe';
  $status[16][32] = 'unknown';
  $status[16][33] = 'unsafe';
  $status[16][34] = 'unsafe';
  $status[16][35] = 'safe';
  $status[16][36] = 'safe';
  $status[16][37] = 'unsafe';
  $status[16][38] = 'unsafe';
  $status[16][39] = 'unsafe';
  $status[16][40] = 'unsafe';
  $status[16][41] = 'unsafe';
  $status[16][42] = 'unsafe';
  $status[16][43] = 'unsafe';
  $status[16][44] = 'unsafe';
  $status[16][45] = 'unsafe';
  $status[16][46] = 'unsafe';
  $status[16][47] = 'safe';
  $status[16][48] = 'safe';
  $status[16][49] = 'safe';
  $status[16][50] = 'safe';
  $status[16][51] = 'unsafe';
  $status[16][52] = 'unsafe';
  $status[16][53] = 'unsafe';
  $status[16][54] = 'unsafe';
  $status[16][55] = 'safe';
  $status[16][56] = 'safe';
  $status[16][57] = 'safe';
  $status[16][58] = 'safe';
  $status[16][59] = 'safe';
  $status[16][60] = 'unsafe';
  $status[17][0] = 'unsafe';
  $status[17][1] = 'unsafe';
  $status[17][2] = 'unsafe';
  $status[17][3] = 'unsafe';
  $status[17][4] = 'unsafe';
  $status[17][5] = 'unsafe';
  $status[17][6] = 'unsafe';
  $status[17][7] = 'unsafe';
  $status[17][8] = 'unsafe';
  $status[17][9] = 'unsafe';
  $status[17][10] = 'unsafe';
  $status[17][11] = 'unsafe';
  $status[17][12] = 'unsafe';
  $status[17][13] = 'unsafe';
  $status[17][14] = 'unsafe';
  $status[17][15] = 'unsafe';
  $status[17][16] = 'unsafe';
  $status[17][17] = 'unsafe';
  $status[17][18] = 'unsafe';
  $status[17][19] = 'unsafe';
  $status[17][20] = 'unsafe';
  $status[17][21] = 'unsafe';
  $status[17][22] = 'unsafe';
  $status[17][23] = 'unsafe';
  $status[17][24] = 'unsafe';
  $status[17][25] = 'unsafe';
  $status[17][26] = 'unsafe';
  $status[17][27] = 'unsafe';
  $status[17][28] = 'unknown';
  $status[17][29] = 'unsafe';
  $status[17][30] = 'unsafe';
  $status[17][31] = 'unsafe';
  $status[17][32] = 'unsafe';
  $status[17][33] = 'unsafe';
  $status[17][34] = 'unsafe';
  $status[17][35] = 'unsafe';
  $status[17][36] = 'unsafe';
  $status[17][37] = 'unsafe';
  $status[17][38] = 'unsafe';
  $status[17][39] = 'unsafe';
  $status[17][40] = 'unsafe';
  $status[17][41] = 'unsafe';
  $status[17][42] = 'unsafe';
  $status[17][43] = 'unsafe';
  $status[17][44] = 'unsafe';
  $status[17][45] = 'unsafe';
  $status[17][46] = 'unsafe';
  $status[17][47] = 'unsafe';
  $status[17][48] = 'unsafe';
  $status[17][49] = 'unsafe';
  $status[17][50] = 'unsafe';
  $status[17][51] = 'unsafe';
  $status[17][52] = 'unsafe';
  $status[17][53] = 'unsafe';
  $status[17][54] = 'unsafe';
  $status[17][55] = 'unsafe';
  $status[17][56] = 'unsafe';
  $status[17][57] = 'unsafe';
  $status[17][58] = 'unsafe';
  $status[17][59] = 'unsafe';
  $status[17][60] = 'unsafe';
  $status[18][0] = 'unsafe';
  $status[18][1] = 'unsafe';
  $status[18][2] = 'unsafe';
  $status[18][3] = 'unsafe';
  $status[18][4] = 'unsafe';
  $status[18][5] = 'unknown';
  $status[18][6] = 'unsafe';
  $status[18][7] = 'unsafe';
  $status[18][8] = 'unsafe';
  $status[18][9] = 'unsafe';
  $status[18][10] = 'unsafe';
  $status[18][11] = 'safe';
  $status[18][12] = 'unsafe';
  $status[18][13] = 'unsafe';
  $status[18][14] = 'unsafe';
  $status[18][15] = 'unsafe';
  $status[18][16] = 'safe';
  $status[18][17] = 'unsafe';
  $status[18][18] = 'unsafe';
  $status[18][19] = 'unsafe';
  $status[18][20] = 'unsafe';
  $status[18][21] = 'unsafe';
  $status[18][22] = 'unsafe';
  $status[18][23] = 'unsafe';
  $status[18][24] = 'unsafe';
  $status[18][25] = 'unsafe';
  $status[18][26] = 'unsafe';
  $status[18][27] = 'unsafe';
  $status[18][28] = 'unsafe';
  $status[18][29] = 'unsafe';
  $status[18][30] = 'unsafe';
  $status[18][31] = 'unsafe';
  $status[18][32] = 'unsafe';
  $status[18][33] = 'unsafe';
  $status[18][34] = 'unsafe';
  $status[18][35] = 'unsafe';
  $status[18][36] = 'unsafe';
  $status[18][37] = 'unsafe';
  $status[18][38] = 'unsafe';
  $status[18][39] = 'unsafe';
  $status[18][40] = 'unsafe';
  $status[18][41] = 'safe';
  $status[18][42] = 'unsafe';
  $status[18][43] = 'safe';
  $status[18][44] = 'unsafe';
  $status[18][45] = 'unsafe';
  $status[18][46] = 'safe';
  $status[18][47] = 'unsafe';
  $status[18][48] = 'safe';
  $status[18][49] = 'unsafe';
  $status[18][50] = 'unsafe';
  $status[18][51] = 'safe';
  $status[18][52] = 'unsafe';
  $status[18][53] = 'safe';
  $status[18][54] = 'unsafe';
  $status[18][55] = 'unsafe';
  $status[18][56] = 'safe';
  $status[18][57] = 'unsafe';
  $status[18][58] = 'safe';
  $status[18][59] = 'unsafe';
  $status[18][60] = 'unsafe';
  $status[19][0] = 'unsafe';
  $status[19][1] = 'unsafe';
  $status[19][2] = 'unsafe';
  $status[19][3] = 'unsafe';
  $status[19][4] = 'unsafe';
  $status[19][5] = 'unknown';
  $status[19][6] = 'unsafe';
  $status[19][7] = 'unsafe';
  $status[19][8] = 'unknown';
  $status[19][9] = 'unsafe';
  $status[19][10] = 'unsafe';
  $status[19][11] = 'unsafe';
  $status[19][12] = 'unsafe';
  $status[19][13] = 'unknown';
  $status[19][14] = 'unsafe';
  $status[19][15] = 'unsafe';
  $status[19][16] = 'unsafe';
  $status[19][17] = 'unsafe';
  $status[19][18] = 'unsafe';
  $status[19][19] = 'unsafe';
  $status[19][20] = 'unsafe';
  $status[19][21] = 'unknown';
  $status[19][22] = 'unsafe';
  $status[19][23] = 'unsafe';
  $status[19][24] = 'unsafe';
  $status[19][25] = 'unsafe';
  $status[19][26] = 'unknown';
  $status[19][27] = 'unsafe';
  $status[19][28] = 'unsafe';
  $status[19][29] = 'unsafe';
  $status[19][30] = 'unsafe';
  $status[19][31] = 'unsafe';
  $status[19][32] = 'unsafe';
  $status[19][33] = 'unsafe';
  $status[19][34] = 'unknown';
  $status[19][35] = 'unsafe';
  $status[19][36] = 'unsafe';
  $status[19][37] = 'unsafe';
  $status[19][38] = 'unsafe';
  $status[19][39] = 'unknown';
  $status[19][40] = 'unknown';
  $status[19][41] = 'unknown';
  $status[19][42] = 'unsafe';
  $status[19][43] = 'unsafe';
  $status[19][44] = 'unknown';
  $status[19][45] = 'unknown';
  $status[19][46] = 'unsafe';
  $status[19][47] = 'unsafe';
  $status[19][48] = 'unknown';
  $status[19][49] = 'unsafe';
  $status[19][50] = 'unsafe';
  $status[19][51] = 'unsafe';
  $status[19][52] = 'unknown';
  $status[19][53] = 'unsafe';
  $status[19][54] = 'unknown';
  $status[19][55] = 'unknown';
  $status[19][56] = 'unsafe';
  $status[19][57] = 'unknown';
  $status[19][58] = 'unknown';
  $status[19][59] = 'unsafe';
  $status[19][60] = 'unsafe';

  return $status;
}
