Ordered collections come in the following interfaces:

- Stacks: support pop, push
- Queues: support enqueue, dequeue
- Stack-queues: is a stack and a queue with pop = dequeue
- Array: supports set and get for some set of indices
- Vector: Resizing array; is a stack-queue and array.
- List: Vector which supports insert, delete

Other methods supported by Array and Vector:

- contains
- reduce(op)
- sorted
- slice
- size (this is actually supported by everything)

# Ways we might implement these:

## Stacks, queue, stack-queue
Concrete vector or linked list are equivalent for the basic care.

If you want to support contains, use a hash set as well.

If you want reductions, check out my `cleverer_ds_gen` file. In some of those cases, this will make the concrete vector or linked list unneccesary.

If you want 
