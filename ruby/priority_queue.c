


VALUE getPriority(VALUE self, VALUE item) {
  long item_int = FIX2LONG(item);

  // this is made up
  magic ms = getStructFromValue(self);

  hashMap1Node *node1 = hashMap__get(ms.hashMap1, item_int);

  return DBL2NUM(hashMapNode__getPriority(ms.)) ??? 
  // do this in `get`
  // if (hashMap1Node == NULL) {
  //   rb_raise_exception("index not found")
  // } else {
  //   return DBL2NUM(node1.priority);
  // }
}

VALUE adjustPriority(VALUE self, VALUE item, VALUE priority) {
  long item_int = FIX2LONG(item);
  double priority_double = RFLOAT_VALUE(priority);

  magic ms = getStructFromValue(self);

  hashMap1Node *node1 = hashMap__get(ms.hashMap1, item_int);


}