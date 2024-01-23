# -*- coding: utf-8 -*-
"""
Created on Sat Oct 27 11:16:13 2018

@author: SHARON ALEXANDER
"""

import nltk
from nltk.sentiment.vader import SentimentIntensityAnalyzer
import pandas as pd
import numpy as np
import pickle
import matplotlib.pyplot as plt

X = pd.read_csv('amazon_meta4_numified.csv')
X = X.iloc[:,1:]

# creating a distinct price menu
mobile_mrp_deal_price = X.loc[:,['mobile_name','mrp','deal_price']].values

mrp = []
deal_price = []
mobileList = []
for r in mobile_mrp_deal_price:
    mobile = r[0]
    if mobile not in mobileList:
        mobileList.append(mobile)
        mrp.append(r[1])
        deal_price.append(r[2])
        
mobile_np = np.array(mobileList).reshape(len(mobileList),1)
mrp_np = np.array(mrp).reshape(len(mrp),1)
deal_price_np = np.array(deal_price).reshape(len(deal_price),1)

price_menu = np.hstack((mobile_np,mrp_np,deal_price_np))
price_menu_list = price_menu.tolist()

#converting the string in dataframe into float
for p in price_menu_list:
    p[1] = float(p[1])
    p[2] = float(p[2])

price_menu_df = pd.DataFrame(price_menu_list,index=price_menu[:,0],columns=['mobile_name','mrp','deal_price'])
price_menu_df.to_csv('price_menu_mobilenameindex.csv')

plt.rcParams['figure.figsize'] = (10,6)
p0 = price_menu_df.plot.bar(title='Amazon Mobile Prices(Rs)')
p0.set(xlabel='Brand',ylabel="Price(Rs)")

#segregate on the basis of mrp
price15 = pd.DataFrame(price_menu_df.iloc[0],columns=['mobile_name','mrp','deal_price'])
price15_30 = pd.DataFrame(price_menu_df.iloc[0],columns=['mobile_name','mrp','deal_price'])
price30_50 = pd.DataFrame(price_menu_df.iloc[0],columns=['mobile_name','mrp','deal_price'])
price50_70 = pd.DataFrame(price_menu_df.iloc[0],columns=['mobile_name','mrp','deal_price'])

for i in range(len(price_menu_df)):
    mrp_df = price_menu_df.iloc[i,1]
    if mrp_df < 15000:
        price15 = price15.append(price_menu_df.iloc[i])
    elif mrp_df>15000 and mrp_df<30000:
        price15_30 = price15_30.append(price_menu_df.iloc[i])
    elif mrp_df>30000 and mrp_df<50000:
        price30_50 = price30_50.append(price_menu_df.iloc[i])
    else:
        price50_70 = price50_70.append(price_menu_df.iloc[i])
        
#ploting different category datas
p1 = price15.plot.bar(title='MRP less than Rs 15000')
p1.set(xlabel='Brand',ylabel="Price(Rs)")

p2 = price15_30.plot.bar(title='MRP between Rs 15000 - 30000')
p2.set(xlabel='Brand',ylabel="Price(Rs)")

p3 = price30_50.plot.bar(title='MRP between Rs 30000 - 50000')
p3.set(xlabel='Brand',ylabel="Price(Rs)")

p4 = price50_70.plot.bar(title='MRP between Rs 50000 - 70000')
p4.set(xlabel='Brand',ylabel="Price(Rs)")

# mrp vs deal_price
mrp = price_menu_df.loc[:,'mrp']
deal_price = price_menu_df.loc[:,'deal_price']
plt.plot(deal_price,mrp,'x')

# Getting emotions of the customers 
# Machine Learnigg Time
nltk.download('vader_lexicon')
sid = SentimentIntensityAnalyzer()

X_np = X.values
mobile_names = dict()
index = 0
emotion_score = []
mobileList = []
count = []
for r in X_np:
    mobile = r[0]
    if mobile not in mobile_names:
        mobile_names[mobile] = index
        mobileList.append(mobile)
        index += 1
        review = r[3]
        e = sid.polarity_scores(review)
        emotion_score.append(np.array([e['neg'],e['neu'],e['pos']]))
        count.append(1)
    else:
        review = r[3]
        e = sid.polarity_scores(review)
        emotion_score[mobile_names[mobile]] += np.array([e['neg'],e['neu'],e['pos']])
        count[mobile_names[mobile]] += 1
        
# calculating average emotion
emotion_score = [emotion_score[i]/count[i] for i in range(len(emotion_score))] 
# converting to list
emotion_score = [emotion_score[i].tolist() for i in range(len(emotion_score))]
# club price categories into list
price_cat = [price15,price15_30,price30_50,price50_70]
s1 = []
s2 = []
s3 = []
s4 = []
score = [s1,s2,s3,s4]
for price in range(len(price_cat)):
    for i in range(len(price_cat[price])):
        mobile1 = price_cat[price].iloc[i,0]  
        idx = mobile_names[mobile1]
        score[price].append(emotion_score[idx])

#saving price cat before making changes to it
name = ['pr15','pr15_30','pr30_50','pr50_70']
for i in range(len(price_cat)):
    filename = name[i] + '.csv'
    price_cat[i].to_csv(filename)


# merging emotion score to the categories
for price in range(len(price_cat)):
    emo = score[price]
    pr = pd.DataFrame(data = emo,index = price_cat[price].loc[:,'mobile_name'],columns = [ 'Negative','Neutral','Positive'])
    price_cat[price] = price_cat[price].join(pr)
    
plt.plot(price_cat[0].loc[:,'Positive'],price_cat[0].loc[:,'Negative'],'x',label='price <= 15K')
plt.plot(price_cat[1].loc[:,'Positive'],price_cat[1].loc[:,'Negative'],'o',label='15K < price <= 30K')
plt.plot(price_cat[2].loc[:,'Positive'],price_cat[2].loc[:,'Negative'],'*',label='30K < price <= 50K')
plt.plot(price_cat[3].loc[:,'Positive'],price_cat[3].loc[:,'Negative'],'+',label='50K < price <= 70K')
plt.xlabel('Positivity ----> ')
plt.ylabel('Negativity ----> ')
plt.title('Emotion Analysis')
plt.legend(loc = 'upper right')
plt.show()

plt.plot(price_cat[0].loc[:,'deal_price'],price_cat[0].loc[:,'mrp'],'x',label='price <= 15K')
plt.plot(price_cat[1].loc[:,'deal_price'],price_cat[1].loc[:,'mrp'],'o',label='15K < price <= 30K')
plt.plot(price_cat[2].loc[:,'deal_price'],price_cat[2].loc[:,'mrp'],'*',label='30K < price <= 50K')
plt.plot(price_cat[3].loc[:,'deal_price'],price_cat[3].loc[:,'mrp'],'+',label='50K < price <= 70K')
plt.xlabel('Deal_price ----> ')
plt.ylabel('MRP ----> ')
plt.title('Deal_price vs Mrp ')
plt.legend(loc = 'bottom right')
plt.show()

    
        
       











