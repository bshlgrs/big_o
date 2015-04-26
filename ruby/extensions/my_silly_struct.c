#include <ruby.h>
#include "extconf.h"

void Init_my_silly_struct()
{
  int state;
  rb_eval_string_protect("puts 'Hello, world!'", &state);
}