#include <ruby.h>
#include "extconf.h"



VALUE my_method(VALUE self, VALUE num) {
  unsigned long myVal = FIX2LONG(num);
  printf("the int is %lu?\n", myVal);

  return LONG2FIX(num);
}

void Init_my_silly_struct()
{
  int state;
  // rb_eval_string_protect("puts 'Hello, world!'", &state);

  VALUE mySillyStruct = rb_define_class("MySillyStruct", rb_cObject);
  rb_define_method(mySillyStruct, "my_method", my_method, 1);
}

