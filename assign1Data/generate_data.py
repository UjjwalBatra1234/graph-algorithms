"""
WARNING: This is a python 3 script to run this script use python3 generate_data.py
"""

import csv, random

num = 0

file_name = "high_density.csv"
with open(file_name, 'w') as csvFile:
    writer = csv.writer(csvFile)
    for src in range(1, 2001):
        for tar in range(1, 2001):
            if src == tar:
                continue
            row = src, tar, random.randint(1, 20)
            num += 1
            writer.writerow(row)

    csvFile.close()

num = 0
file_name = "medium_density.csv"
with open(file_name, 'w') as csvFile:
    writer = csv.writer(csvFile)
    for src in range(1, 2001):
        for tar in range(1, 1001):
            if src == tar:
                continue
            row = src, tar, random.randint(1, 20)
            num += 1
            writer.writerow(row)

    csvFile.close()
