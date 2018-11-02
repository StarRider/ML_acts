# ML_acts
In this repository I am posting the machine learning work which I am doing to expand my knowledge.
This respository has some web scraped data and it's sentiment analysis report.
# Web_scarp
The data is scraped from Amazon website using chrome web scraper. 
# Pre-processed data
1. The data scrapped had many missing elements. So removal of such rows where done. (amazon_meta4_numified.csv)
2. Converted all the string formated price to numerical format like float.
# Using nltk vader
nltk is a python library for natural language processing. It has an inbuilt sentiment analysis model named vader.
Then using vader's sentiment analysis functions I generated the plot for people's emotion towards each mobile phone brand.
# Plots
I generated 5 plots.
1. Plot 1 for showing all the deal_price vs mrp of mobiles
2. Then I created the categories based on mrp and plotted them too.<br>
  2.1 less than 15K.<br>
  2.2 between 15K to 30K.<br>
  2.3 between 30K to 50K.<br>
  2.4 between 50K to 70K.<br>
3. Finally the ML result the emotion analysis plot.
# The JAVA Dashboard
The ML part was done using python. Further I created a dashboard using JAVA, which presents all the analysis in an attractive way.
The java program uses a text file to fetch all the plots.<br>
Java program file: DataBoard.java
Text file used: myFile.txt
