# Conjectures

The optimal data structure to support quick queries about a reduction is the same as an optimal data structure to deal with questions about reductions of sublists. Eg, the same data structure is the answer for the question "Write an array which support set and get and sum" is also optimal for "Write an array which supports set, get, sum, and \i j -> array[i:j].sum".