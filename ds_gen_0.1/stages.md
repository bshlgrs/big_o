# stages of data structure design

## Specification
We start out with our specification.

## Abstract data structures
We rewrite this into a form like this:

get, set, insert, remove => [IndexTreeList(get, set, insert, remove)]

But this doesn't capture the most complex things we'd want to express. We want to be able to express things like "this hash map includes a list of indices in that heap, so that it's faster to look up the priority of things."

Ideally we should also be able to express things like "this index tree also stores the partial sum in each node".

## Generic code

Function calls or whatever. Hopefully still using explicitly higher order functions at this point.

## Specific code

actually C++ or whatever.