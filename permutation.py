# This creates randomized permutation table

import random

numbers = []
length = 256

count = 0
while count < length:
    numbers.append (count)
    count += 1


count = 0
while count < length:
    r = random.randint (count, length - 1)
    temp = numbers [r]
    numbers [r] = numbers[count]
    numbers [count] = temp
    count += 1

step = 16
count = 0
while count < length:
    stepcount = 0
    line = ""
    while stepcount < step:
        line += "{},".format (numbers[count + stepcount])
        stepcount += 1
    print (line)
    count += step
