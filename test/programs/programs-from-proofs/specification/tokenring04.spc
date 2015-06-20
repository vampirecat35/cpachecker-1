OBSERVER AUTOMATON Monitor3Automaton

INITIAL STATE Init;

STATE USEFIRST Init :
  MATCH  "t1_started();"  -> GOTO Start1000;
  MATCH  "t2_started();"  -> GOTO Start0100;
  MATCH  "t3_started();"  -> GOTO Start0010;
  MATCH  "t4_started();"  -> GOTO Start0001;

STATE USEFIRST Start1000 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> GOTO Start1100;
  MATCH  "t3_started();"  -> GOTO Start1010;
  MATCH  "t4_started();"  -> GOTO Start1001;

STATE USEFIRST Start0100 :
  MATCH  "t1_started();"  -> GOTO Start1100;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> GOTO Start0110;
  MATCH  "t4_started();"  -> GOTO Start0101;

STATE USEFIRST Start0010 :
  MATCH  "t1_started();"  -> GOTO Start1010;
  MATCH  "t2_started();"  -> GOTO Start0110;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> GOTO Start0011;

STATE USEFIRST Start1100 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> GOTO Start1110;
  MATCH  "t4_started();"  -> GOTO Start1101;

STATE USEFIRST Start1010 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> GOTO Start1110;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> GOTO Start1011;

STATE USEFIRST Start0110 :
  MATCH  "t1_started();"  -> GOTO Start1110;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> GOTO Start0111;

STATE USEFIRST Start0101 :
  MATCH  "t1_started();"  -> GOTO Start1101;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> GOTO Start0111;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start1001 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> GOTO Start1101;
  MATCH  "t3_started();"  -> GOTO Start1011;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start0011 :
  MATCH  "t1_started();"  -> GOTO Start1011;
  MATCH  "t2_started();"  -> GOTO Start0111;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start0001 :
  MATCH  "t1_started();"  -> GOTO Start1001;
  MATCH  "t2_started();"  -> GOTO Start0101;
  MATCH  "t3_started();"  -> GOTO Start0011;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start1011 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> GOTO Init;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start1110 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> GOTO Init;

STATE USEFIRST Start1101 :
  MATCH  "t1_started();"  -> ERROR;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> GOTO Init;
  MATCH  "t4_started();"  -> ERROR;

STATE USEFIRST Start0111 :
  MATCH  "t1_started();"  -> GOTO Init;
  MATCH  "t2_started();"  -> ERROR;
  MATCH  "t3_started();"  -> ERROR;
  MATCH  "t4_started();"  -> ERROR;

END AUTOMATON
