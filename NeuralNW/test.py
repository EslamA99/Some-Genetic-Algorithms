import numpy as np
import pandas as pd


def sigmoid(z):
    return 1 / (1 + np.exp(-z))


def forwardPropagation(X, W_H, W_O):
    Net_H = np.dot(W_H, np.array([X]).transpose()).astype(float)
    A_H = sigmoid(Net_H)
    Net_O = np.dot(W_O, A_H).astype(float)
    A_O = sigmoid(Net_O)
    return A_H, A_O


def clacAccurcy(y, a3):
    m = len(y)
    error = np.subtract(y, a3)
    sqr_error = np.multiply(error, error)
    mean_sqr_error = np.sum(sqr_error) / m
    return mean_sqr_error


df = np.array(pd.read_csv('output.txt'))
df = np.delete(df, 1, axis=1)
wh = []
wo = []
for i in df:
    if i[0] == 'wh':
        row = np.array(np.delete(i, 0))
        row = row[np.logical_not(pd.isnull(row))]
        wh.append(row)
    else:
        row = np.delete(i, 0)
        wo.append(row)
W_H = np.array(wh)
W_O = np.array(wo)

f = open("train.txt", "r")
firstLine = f.readline().split()
inputNodes = int(firstLine[0])
hiddenNodes = int(firstLine[1])
outputNodes = int(firstLine[2])
m = int(f.readline().split()[0])
dataFile = np.loadtxt("train.txt", skiprows=2)
X = np.array(dataFile[:, 0:inputNodes])
Y = np.array(dataFile[:, inputNodes:(inputNodes + outputNodes)])
H = []

for i in range(inputNodes):
    X[:, i:i + 1] = (X[:, i:i + 1] - min(X[:, i:i + 1]) ) / (max(X[:, i:i + 1]) - min(X[:, i:i + 1]))
for i in range(outputNodes):
    Y[:, i:i + 1] = (Y[:, i:i + 1] - min(Y[:, i:i + 1]) ) / (max(Y[:, i:i + 1]) - min(Y[:, i:i + 1]))

for i in range(X.shape[0]):
    A_H, A_O = forwardPropagation(X[i], W_H, W_O)
    H.append(A_O[0])
    print(A_O[0], Y[i])
print("MSE= ",clacAccurcy(Y, H))
print("-----------------------------------------------------------------------")
