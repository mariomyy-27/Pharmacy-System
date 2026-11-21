package sample.service;

import sample.model.Sale;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalesService {

    private final String filePath = "Week3/Data/sales.txt";

    public List<Sale> loadSales() {

        List<Sale> sales = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                sales.add(new Sale(
                        parts[0],
                        Double.parseDouble(parts[1]),
                        Integer.parseInt(parts[2])
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return sales;
    }

    public void addSale(Sale sale) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            writer.write(
                    sale.getMedicineName() + "," +
                    sale.getPrice() + "," +
                    sale.getAmountSold()
            );

            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}