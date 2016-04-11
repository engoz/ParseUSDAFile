##PARSE Nutrient DATA file from USDA

##General Requirements

You'll need a Nutrient data files( NUT_DATA.txt,WEIGHT.txt, FOOD_DES.txt ). 
All files must be in data folder in this project 

##Running
Run ParseNutrient class main method.

##Result
result combination data in db.tsv and weight.tsv

db.tsv format

FOOD_DES.NDB_No
FOOD_DES.FdGrp_Cd
FOOD_DES.Long_Desc
FOOD_DES.Shrt_Desc
FOOD_DES.ComName
FOOD_DES.ManufacName
FOOD_DES.Survey
NUT_DATA.Nutr_No
NUT_DATA.Nutr_Val

weigth.tsv

SEQUENTIAL KEY
WEIGHT.NDB_No
WEIGHT.Seq
WEIGHT.Amount
WEIGHT.Msre_Desc
WEIGHT.Gm_Wgt

##Known issues

Y and N are blank.


 




