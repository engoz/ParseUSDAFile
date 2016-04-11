package nutrient.parse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author enginoz
 * Created by enginoz on 4/10/16.
 * Parse Nutrient Data File form USDA
 * @version 1.0
 */
public class ParseNutrient {
    private static final String columnDelimiter = "^";
    private static final String stringDelimiter = "~";
    private static final String nullOrBlank = "~~";
    private static final String nullValue = "^^";
    private static StringTokenizer st;
    private static Map<Integer, FoodDescription> foodDestinationMap = new HashMap<>();
    private static Map<Integer, Weight> weightMap = new HashMap<>();
    private static Map<Integer,NutrientData> nutrientDataMap = new HashMap<>();


    public static void main(String[] args) {

        writeWeight();
        writeFoodDestAndData();


    }

    private static void writeWeight() {
                weightMap = parseWeigth();
                writeWeightMap(weightMap);

    }

    private static void writeFoodDestAndData() {
                foodDestinationMap = parseFoodDescription();
                nutrientDataMap = parseNutrientData();
                mergeToMapAndWriteTSV(foodDestinationMap, nutrientDataMap);
    }

    /*
    * @writeWeightMap write weight.tsv file from map
    * @param weightMap
    * */
    public static void writeWeightMap(Map<Integer, Weight> weightMap) {
        if (weightMap.size() > 0) {
            try (BufferedWriter bfw = new BufferedWriter(new FileWriter(new File("data/weight.tsv")))) {
                int line = 0;
                for (Map.Entry<Integer, Weight> entry : weightMap.entrySet()) {
                    Weight weight = entry.getValue();
                    StringBuilder sb = new StringBuilder();
                    //generated sequential key
                    sb.append(entry.getKey());
                    sb.append('\t');
                    //5-digit Nutrient Databank number that uniquely identifies a food item.
                    sb.append(weight.getNdbNo());
                    sb.append('\t');
                    //Sequence number.
                    sb.append(weight.getSequence());
                    sb.append('\t');
                    //Unit modifier (for example, 1 in “1 cup”).
                    sb.append(weight.getAmount());
                    sb.append('\t');
                    //Description (for example, cup, diced, and 1-inchpieces).
                    sb.append(weight.getUnits());
                    sb.append('\t');
                    //Gram weight.
                    sb.append(weight.getGrams());
                    sb.append('\n');
                    bfw.write(sb.toString());
                    System.out.println("Write Line Weight " + line);
                    line++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*
    * @mergeToMapAndWriteTSV two map combine with uniq id and write db.tsv file
    * @param foodDestinationMap
    * @param nutrientDataMap
    * */
    private static void mergeToMapAndWriteTSV(Map<Integer, FoodDescription> foodDestinationMap, Map<Integer, NutrientData> nutrientDataMap) {
        if (foodDestinationMap.size() > 0 && nutrientDataMap.size() > 0) {
            try (BufferedWriter bwM = new BufferedWriter(new FileWriter(new File("data/db.tsv")))) {
                int line = 0;
                for (Map.Entry<Integer, FoodDescription> entry : foodDestinationMap.entrySet()) {
                    FoodDescription foodDescription = entry.getValue();
                    NutrientData nutData = nutrientDataMap.get(entry.getKey());
                    if (foodDescription != null && nutData != null) {
                        StringBuilder sb = new StringBuilder();
                        //5-digit Nutrient Databank number that uniquely
                        sb.append(foodDescription.getNdbNo());
                        sb.append('\t');
                        //4-digit code indicating food group
                        sb.append(foodDescription.getFoodGroup());
                        sb.append('\t');
                        //200-character description of food item
                        sb.append(foodDescription.getLongDecs());
                        sb.append('\t');
                        //60-character abbreviated description
                        sb.append(foodDescription.getShrtDesc());
                        sb.append('\t');
                        //Other names commonly used to describe a food
                        sb.append(foodDescription.getComName());
                        sb.append('\t');
                        //Indicates the company that manufactured theproduct, when appropriate.
                        sb.append(foodDescription.getManufacName());
                        sb.append('\t');
                        //Indicates if the food item is used in the USDA Food and Nutrient Database for Dietary Studies
                        sb.append(foodDescription.getSurvey());
                        sb.append('\t');
                        //Unique 3-digit identifier code for a nutrient.
                        sb.append(nutData.getNutrNo());
                        sb.append('\t');
                        //Amount in 100 grams, edible portion †.
                        sb.append(nutData.getNutrVal());
                        sb.append('\n');
                        bwM.write(sb.toString());
                        System.out.println("Write Line Food " + line);
                        line++;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /*
     * @parseFoodDescription parse FoodDescription Data file FOOD_DES.txt
     * */
    private static Map<Integer, FoodDescription> parseFoodDescription() {
        Map<Integer, FoodDescription> tmpTemp = new HashMap<>();
        try (BufferedReader brF = new BufferedReader(new FileReader(FoodDescription.FILE_NAME))) {
            String line;
            FoodDescription foodDescription;
            while ((line = brF.readLine()) != null) {
                st = new StringTokenizer(line, columnDelimiter);
                foodDescription = new FoodDescription();
                int count = st.countTokens();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        String s = st.nextToken();
                        switch (i) {
                            case FoodDescription.NDB_NO:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setNdbNo(Integer.valueOf(s));
                                }
                            case FoodDescription.FOOD_GROUP:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setFoodGroup(Integer.valueOf(s));
                                }
                                break;
                            case FoodDescription.LONG_DESC:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setLongDecs(s);
                                }
                                break;
                            case FoodDescription.SHRT_DESC:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setShrtDesc(s);
                                }
                                break;
                            case FoodDescription.COM_NAME:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setComName(s);
                                }

                                break;
                            case FoodDescription.MANUFACNAME:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setManufacName(s);
                                }

                                break;
                            case FoodDescription.SURVEY:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    foodDescription.setSurvey(s);
                                }

                                break;
                            default:
                                break;

                        }
                    }
                }
                tmpTemp.put(foodDescription.getNdbNo(), foodDescription);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpTemp;
    }


        /*
       * @parseWeigth() parse Weight Data file Weight.txt
       * */
    private static Map<Integer, Weight> parseWeigth() {
        Map<Integer, Weight> tmpTemp = new HashMap<>();
        try (BufferedReader brW = new BufferedReader(new FileReader(Weight.FILE_NAME))) {
            String line;
            Weight weight;
            int id = 0;
            while ((line = brW.readLine()) != null) {
                st = new StringTokenizer(line, columnDelimiter);
                weight = new Weight();
                int count = st.countTokens();
                for (int i = 0; i < count; i++) {
                    String s = st.nextToken();
                    switch (i) {
                        case Weight.NDB_NO:
                            if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                s = s.substring(1, s.length() - 1);
                                weight.setNdbNo(Integer.valueOf(s));
                            }
                        case Weight.SEQ:
                            weight.setSequence(Integer.valueOf(s));
                            break;
                        case Weight.AMOUNT:
                            weight.setAmount(Double.valueOf(s));
                            break;
                        case Weight.UNITS:
                            if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                s = s.substring(1, s.length() - 1);
                                weight.setUnits(s);
                            }
                            break;
                        case Weight.GRAMS:
                            weight.setGrams(Double.valueOf(s));
                            break;
                        default:
                            break;

                    }
                }
                tmpTemp.put(id++, weight);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpTemp;
    }


        /*
       * @parseNutrientData() parse Nutrient Data NUT_DATA
       * */

    private static Map<Integer,NutrientData> parseNutrientData() {
        Map<Integer, NutrientData> tmpTemp = new HashMap<>();
        try (BufferedReader brD = new BufferedReader(new FileReader(NutrientData.FILE_NAME))) {
            String line;
            int id = 0;
            Map<Integer, NutrientData> dataMap = new HashMap<>();
            while ((line = brD.readLine()) != null) {
                st = new StringTokenizer(line, columnDelimiter);
                NutrientData nutrientData = new NutrientData();
                int count = st.countTokens();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        String s = st.nextToken();
                        switch (i) {
                            case NutrientData.NDB_NO:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    if (Integer.valueOf(s) != id) {
                                        id = Integer.valueOf(s);
                                        nutrientData.setNdbNo(id);
                                    }
                                }
                                break;
                            case NutrientData.NUTR_NO:
                                if (s.startsWith(stringDelimiter) && s.endsWith(stringDelimiter)) {
                                    s = s.substring(1, s.length() - 1);
                                    if (Integer.valueOf(s) == 208) {
                                        nutrientData.setNutrNo(Integer.valueOf(s));
                                    }
                                }
                                break;
                            case NutrientData.NUTR_VAL:
                                    if (nutrientData.getNutrNo() == 208) {
                                        nutrientData.setNutrVal(Double.valueOf(s));
                                    }
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (nutrientData.getNutrNo() == 208) {
                    tmpTemp.put(id, nutrientData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpTemp;
    }

    //Food Description Object
    protected static class FoodDescription {
        public static final String FILE_NAME = "data/FOOD_DES.txt";
        public static final int NDB_NO = 0;
        public static final int FOOD_GROUP = 1;
        public static final int LONG_DESC = 2;
        public static final int SHRT_DESC = 3;
        public static final int COM_NAME = 4;
        public static final int MANUFACNAME = 5;
        public static final int SURVEY = 6;

        private int ndbNo;
        private int foodGroup;
        private String longDecs;
        private String shrtDesc;
        private String comName;
        private String manufacName;
        private String survey;


        public int getNdbNo() {
            return ndbNo;
        }

        public void setNdbNo(int ndbNo) {
            this.ndbNo = ndbNo;
        }

        public int getFoodGroup() {
            return foodGroup;
        }

        public void setFoodGroup(int foodGroup) {
            this.foodGroup = foodGroup;
        }

        public String getLongDecs() {
            return longDecs;
        }

        public void setLongDecs(String longDecs) {
            this.longDecs = longDecs;
        }

        public String getShrtDesc() {
            return shrtDesc;
        }

        public void setShrtDesc(String shrtDesc) {
            this.shrtDesc = shrtDesc;
        }

        public String getComName() {
            return comName;
        }

        public void setComName(String comName) {
            this.comName = comName;
        }

        public String getManufacName() {
            return manufacName;
        }

        public void setManufacName(String manufacName) {
            this.manufacName = manufacName;
        }

        public String getSurvey() {
            return survey;
        }

        public void setSurvey(String survey) {
            this.survey = survey;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if(!(obj instanceof FoodDescription))
                return false;
            FoodDescription f = (FoodDescription) obj;
            return f.ndbNo == ndbNo && f.foodGroup == foodGroup;

        }

        private volatile int hashCode;

        @Override public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = 17;
                result = 31 * result + ndbNo;
                result = 31 * result + foodGroup;
                hashCode = result;
            }
            return result;
        }
    }

    //Weight Object
    protected static final class Weight {
        public static final String FILE_NAME = "data/WEIGHT.txt";
        public static final int NDB_NO = 0;
        public static final int SEQ = 1;
        public static final int AMOUNT = 2;
        public static final int UNITS = 3;
        public static final int GRAMS = 4;

        private int ndbNo;
        private int sequence;
        private double amount;
        private String units;
        private Double grams;

        public int getNdbNo() {
            return ndbNo;
        }

        public void setNdbNo(int ndbNo) {
            this.ndbNo = ndbNo;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public Double getGrams() {
            return grams;
        }

        public void setGrams(Double grams) {
            this.grams = grams;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if(!(obj instanceof Weight))
                return false;
            Weight w = (Weight) obj;
            return w.ndbNo == ndbNo && w.amount == amount && w.grams == grams;

        }

        private volatile int hashCode;

        @Override public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = 17;
                result = 31 * result + ndbNo;
                result = 31 * result + sequence;
                hashCode = result;
            }
            return result;
        }
    }

    //Nutrient Data Object
    protected static final class NutrientData {
        public static final String FILE_NAME = "data/NUT_DATA.txt";
        public static final int NDB_NO = 0;
        public static final int NUTR_NO = 1;
        public static final int NUTR_VAL = 2;

        private int ndbNo;
        private int nutrNo;
        private double nutrVal;

        public int getNdbNo() {
            return ndbNo;
        }

        public void setNdbNo(int ndbNo) {
            this.ndbNo = ndbNo;
        }

        public int getNutrNo() {
            return nutrNo;
        }

        public void setNutrNo(int nutrNo) {
            this.nutrNo = nutrNo;
        }

        public double getNutrVal() {
            return nutrVal;
        }

        public void setNutrVal(double nutrVal) {
            this.nutrVal = nutrVal;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if(!(obj instanceof NutrientData))
                return false;
            NutrientData n = (NutrientData) obj;
            return n.ndbNo == ndbNo && n.nutrNo == nutrNo && n.nutrVal == nutrVal;

        }

        private volatile int hashCode;

        @Override public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = 17;
                result = 31 * result + ndbNo;
                result = 31 * result + nutrNo;
                hashCode = result;
            }
            return result;
        }
    }
}
