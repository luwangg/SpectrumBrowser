'''
Created on Feb 2, 2015

@author: local
'''

from pymongo import MongoClient
import Bootstrap
import pymongo



def initConnections():
    global db
    global admindb
    global occpancydb
    global sysconfigdb
    if not "_dbConnectionsInitialized" in globals():
        global _dbConnectionsInitialized
        _dbConnectionsInitialized = True
        mongodb_host = Bootstrap.getDbHost()
        client = MongoClient(mongodb_host)
        db = client.spectrumdb
        admindb = client.admindb
        sysconfigdb = client.sysconfig
        occpancydb = client.occpancydb

######################################################################################
# Access to globals should go through here.
def getAccounts():
    initConnections()
    global admindb
    return admindb.accounts

def getTempAccounts():
    initConnections()
    global admindb
    return admindb.tempaccounts

def getSpectrumDb():
    initConnections()
    global db
    return db

def getDataMessages(sensorId):
    if "dataMessages." + sensorId in getSpectrumDb().collection_names():
        return getSpectrumDb()["dataMessages." + sensorId]
    return getSpectrumDb().create_collection("dataMessages." + sensorId)

def dropDataMessages(sensorId):
    getSpectrumDb().drop_collection("dataMessages." + sensorId)

def getSystemMessages():
    initConnections()
    global db
    return db.systemMessages

def getLocationMessages():
    initConnections()
    global db
    return db.locationMessages

def getTempPasswords():
    initConnections()
    global admindb
    return admindb.tempPasswords

def getSensors():
    initConnections()
    global admindb
    return admindb.sensors

def getTempSensorsCollection():
    initConnections()
    global admindb
    return admindb.tempSensors

def getPeerConfigDb():
    initConnections()
    global sysconfigdb
    return sysconfigdb.peerconfig

def getSysConfigDb():
    initConnections()
    global admindb
    return sysconfigdb.configuration

def getScrConfigDb():
    initConnections()
    global sysconfigdb
    return sysconfigdb.scrconfig

def initIndexes():
    getSystemMessages().ensure_index("t", pymongo.DESCENDING)
    getLocationMessages().ensure_index("t", pymongo.DESCENDING)

