import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Operations {
    private HashTable<String,CustomerData> database = Test.database;
    private long avgSearchTime=0;
    private long minSearchTime = Integer.MAX_VALUE;
    private long maxSearchTime = 0;
    public Operations(){
        database.performanceMonitoring(0.5, true, true);
        System.out.println("load factor = 0.5, SSF,DH");
        long startTime = System.currentTimeMillis();
        setDatabase();
        long endTime = System.currentTimeMillis();
        database.getValue("dabf5ded-dc94-48c9-8e9d-c522b976aaa1").printCustomerData();
        System.out.println(database.getSize());
        /*System.out.println("Indexing time is: " + (endTime - startTime) + "ms");
        System.out.println("Collision count is: " + database.getCollision());
        
        searchCustomer();
        
        System.out.println("average search time: " + avgSearchTime + " ns");
        System.out.println("min search time: " + minSearchTime + " ns");
        System.out.println("max search time: " + maxSearchTime + " ns");*/
    }

    private void setDatabase(){// fills hashtable from csv file
		String line;
		String key;
        String[] splittedLine = new String[4];
        int index = 0;
        String path ="supermarket_dataset_50K.csv";
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                index++;
                splittedLine = line.split(",");
                key = splittedLine[0];
                CustomerData value =database.getValue(key);
                if(value == null ){
                    CustomerData data = new CustomerData(splittedLine[1], splittedLine[2], splittedLine[3]);
                    database.add(key, data);
                }else{
                    value.addNewOrder(splittedLine[2], splittedLine[3]);
                    database.add(key, value);
                }
            }
            
        } catch (IOException e) {
            System.out.println(
			"Error while reading a file.");
		}
    }

    private void searchCustomer(){//it reads customer keys from txt file, and takes the searchtimes. Calculates average, max and min search time.
        String line;
        int count = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader("customer_1K.txt"))) {
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                long searchTime= searchPerformanceTest(line);
                if(searchTime < minSearchTime){
                    minSearchTime = searchTime;
                }else if(searchTime> maxSearchTime){
                    maxSearchTime = searchTime;
                }
                avgSearchTime += searchTime;
                count++;
            }
            avgSearchTime /= count;
            
            
        } catch (IOException e) {
            System.out.println(
			"Error while reading a file.");
		}
    }

    private long searchPerformanceTest(String key) {// returns the working time of the getvalue function for spesific key
        long startTime = System.nanoTime();
        database.getValue(key);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        return elapsedTime;
        
    }
    
}
