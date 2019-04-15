"""
WARNING: This is a python 3 script to run this script use python3 generate_data.py
"""

import csv, random

file_name = "high_density.csv"
with open(file_name, 'w') as csvFile:
    writer = csv.writer(csvFile)
    for src in range(1, 101):
        for tar in range(1, 101):
            if src == tar:
                continue
            row = src, tar, random.randint(1, 20)
            writer.writerow(row)

    csvFile.close()

file_name = "medium_density.csv"
with open(file_name, 'w') as csvFile:
    writer = csv.writer(csvFile)
    for src in range(1, 101):
        for tar in range(1, 51):
            if src == tar:
                continue
            row = src, tar, random.randint(1, 20)
            writer.writerow(row)

    csvFile.close()

file_name = "low_density.csv"
with open(file_name, 'w') as csvFile:
    writer = csv.writer(csvFile)
    for src in range(1, 100):
        row = src, src + 1, random.randint(1, 20)
        writer.writerow(row)

    csvFile.close()
