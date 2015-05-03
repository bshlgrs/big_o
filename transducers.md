If I say mylist.map(_ * 3).filter(_ > 12).reduce(:+), this can be implemented as:

total = 0
for x in mylist:
  newthing = x * 3
  if newthing > 12:
    total += newthing

For bonus points, we can go over whatever different data structures we want.
