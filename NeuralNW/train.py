import pandas as pd
import numpy as np


def initialize(inputNodes, hiddenNodes, outputNodes):
    W_H = np.random.randn(hiddenNodes, inputNodes)
    W_O = np.random.randn(outputNodes, hiddenNodes)
    return W_H, W_O


def sigmoid(z):
    return 1 / (1 + np.exp(-z))


def forwardPropagation(X, W_H, W_O):
    Net_H = np.dot(W_H, np.array([X]).transpose()).astype(float)
    A_H = sigmoid(Net_H)
    Net_O = np.dot(W_O, A_H).astype(float)
    A_O = sigmoid(Net_O)
    return A_H, A_O


def backProbagation( Y, W_O, A_H, A_O):
    sigmaO = np.subtract(A_O, Y) * A_O * (1 - A_O)
    sigmaH = np.multiply(np.array(np.multiply(sigmaO, W_O)).transpose(), A_H * (1 - A_H))
    return sigmaH, sigmaO


def update(alpha, W_H, W_O, sigma_H, sigma_O, X, A_H):
    updatedW_O = W_O - (np.array(np.multiply(np.multiply(alpha, sigma_O), A_H)).transpose())
    updatedW_H = np.subtract(W_H, np.multiply(alpha, np.multiply(sigma_H, X)))
    return updatedW_H, updatedW_O


def clacAccurcy(y, a3):
    m = len(y)
    error = np.subtract(y, a3)
    sqr_error = np.multiply(error, error)
    mean_sqr_error = np.sum(sqr_error) / m
    return mean_sqr_error


f = open("train.txt", "r")
firstLine = f.readline().split()
inputNodes = int(firstLine[0])
hiddenNodes = int(firstLine[1])
outputNodes = int(firstLine[2])
m = int(f.readline().split()[0])
dataFile = np.loadtxt("train.txt", skiprows=2)
X = np.array(dataFile[:, 0:inputNodes])
Y = np.array(dataFile[:, inputNodes:(inputNodes + outputNodes)])
for i in range(inputNodes):
    X[:, i:i + 1] = (X[:, i:i + 1] - min(X[:, i:i + 1]) ) / (max(X[:, i:i + 1]) - min(X[:, i:i + 1]))
# X = np.concatenate((np.ones((X.shape[0], 1)), X), axis=1)
for i in range(outputNodes):
    Y[:, i:i + 1] = (Y[:, i:i + 1] - min(Y[:, i:i + 1]) ) / (max(Y[:, i:i + 1]) - min(Y[:, i:i + 1]))

# inputNodes = inputNodes + 1
alpha = 0.3
noItr = 500
H = []
W_H, W_O = initialize(inputNodes, hiddenNodes, outputNodes)
for itr in range(noItr):
    for i in range(X.shape[0]):
        A_H, A_O = forwardPropagation(X[i], W_H, W_O)
        sigmaH, sigmaO = backProbagation( Y[i], W_O, A_H, A_O)
        updatedW_H, updatedW_O = update(alpha, W_H, W_O, sigmaH, sigmaO, X[i], A_H)
        W_H = updatedW_H
        W_O = updatedW_O
for i in range(X.shape[0]):
    A_H, A_O = forwardPropagation(X[i], W_H, W_O)
    H.append(A_O[0])
    print(A_O[0], Y[i])

print("MSE= ",clacAccurcy(Y, H))
print("-----------------------------------------------------------------------")
pd.concat([pd.DataFrame(W_H), pd.DataFrame(W_O)], keys=['wh', 'wo'], axis=0, names=['Type', 'Row ID']).to_csv(
    'output.txt', header=True)

'''for i in range(inputNodes):
    X[:, i:i + 1] = (X[:, i:i + 1] - X[:, i:i + 1].mean()) / X[:, i:i + 1].std()
# X = np.concatenate((np.ones((X.shape[0], 1)), X), axis=1)
for i in range(outputNodes):
    Y[:, i:i + 1] = (Y[:, i:i + 1] - Y[:, i:i + 1].mean()) / Y[:, i:i + 1].std()'''
