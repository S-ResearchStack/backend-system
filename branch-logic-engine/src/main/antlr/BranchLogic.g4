grammar BranchLogic;

@header { package com.samsung.healthcare.branchlogicengine; }

expression
    : logicalExpression
    | comparisonExpression
    | membershipExpression
    ;

logicalExpression : op=logicalOperator ' ' left=expression ' ' right=expression;
comparisonExpression : op=comparisonOperator ' ' left=identifier ' ' right=Value;
membershipExpression : op=membershipOperator ' ' left=identifier ' ' right=Value;

logicalOperator
    : AND | OR
    ;

comparisonOperator
    : GT | GTE | LT | LTE | EQ | NEQ
    ;

membershipOperator
    : CONTAINS | NCONTAINS ;

identifier
    : VAR_ANS
    | VAR_CNT
    ;

VAR_ANS: 'val' Integer;
VAR_CNT: 'cnt' Integer;

Value
    : Sign? Float
    | Sign? Integer
    | String
    ;

Float
    :  Integer? '.' Integer
    |  Integer '.' Integer?
    ;
Integer : Digit+;
String : '"' Char* '"';

fragment Char : ~'"' | '\\"' ;
fragment Digit : [0-9];
fragment Sign : '-' | '+';

AND: 'and';
OR: 'or';
GT: 'gt';
GTE: 'gte';
LT: 'lt';
LTE: 'lte';
EQ: 'eq';
NEQ: 'neq';
CONTAINS: 'contains';
NCONTAINS: 'notcontains';
