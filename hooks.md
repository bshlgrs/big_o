# Hooks

It's obvious that somehow we need to express the idea that certain functions need to be called whenever you update your data structure.

(How about in cases like splay trees where the tree changes even on reads?)

I think we should probably do this with hooks. So your data structure specifies functions to be called on it when various things happen.

We want the hook's action to be carried out lots of different ways though. We can possibly do this using the same notation for alternatives as we were already planning.

