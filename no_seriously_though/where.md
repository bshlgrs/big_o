# Where

I'm interested in finding general answers to data structure design questions, because I'm currently working on a system which automatically implements fast data structures given an API. This is a piece of the puzzle which I figured out today:

I'm trying to automate the process of choosing data structures which enable a particular kind of query over your data. In this setting, when you're choosing data structures you know what queries you'll receive, but you don't know their parameters.

For example, suppose your data is a list of people, and you plan to support an addPerson operation as well as some of the following queries:

- P1: Given `x`, find the average height of people of gender `x` in the data set. (Gender is a string field.)
- P2: Given `x` and `y`, find the mean weight of men taller than `x` whose age is `y`.
- P3: Given `x` and `y`, find the average age of men whose heights are between `x` and `y`.
- P4: Given `x` and `y`, find the age of the person whose gender is `x` and whose age is over `y` with the largest BMI.

I can do all of the above in `log(n)` time, where `n` is the number of people, which I think is optimal (unless you start cheating by bucketing ages or heights or whatever.)

I've pieced together a few different data structures to come up with a unified solution to all of these. I call it: a _monoid-memoizing k-d tree forest_. Here's how we use it to solve all of the above problems.

## Step 1: separate your restrictions out into seperate predicate clauses.

Think of this as the WHERE clauses in SQL. Maybe I'll formalize this more carefully later.

For P1, we just have the one restriction, `gender = x`

For P2, we have three restrictions: `gender = "male"`, `height > x`, `age = y`

For P3, we have three restrictions: `gender = "male"`, `height > x`, `height < y`

For P4, we have two restrictions: `gender = x`, `age > y`. We'll deal with the "highest BMI" thing later.

## Step 2: categorize these:

There are four different categories of restrictions that we care about.

The simplest is things like `gender = "male"` from P2 above. This restriction is known when we're writing our code, so we can just ignore non-male people in our data structure. Any restriction which we know completely when we're writing our code falls into this category.

The next is things like `height > x`. These are an inequality between a function of our data, and a parameter.

Third are things like `height = x`. These are equalities between a parameter and a function of the data.

Fourth are things that don't fit into the above. In this case we give up. More on this later.

In our running example, the classification of the restrictions is:

P1: 3

P2: 1, 2, 3

P3: 1, 3, 3

P4: 3, 2

## 