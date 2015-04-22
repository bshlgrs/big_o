# advanced data structures

- bloom filters
- treaps, random binary trees
- union find with constant time deletion http://www.cs.princeton.edu/courses/archive/fall05/cos528/handouts/Union-Find%20with%20Constant%20Time%20Deletions.pdf
- splitting data records for better cache behavior: http://research.microsoft.com/en-us/um/people/trishulc/papers/Cache-conscious.pdf
	- defines a malloc called ccmalloc which is cache-conscious and tries to allocate memory in consecutive blocks if it's going to be accessed at the same time. 
- use these: http://stackoverflow.com/questions/668501/are-there-any-open-source-c-libraries-with-common-data-structures
- 