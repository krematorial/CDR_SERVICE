package kulagoSA.CDRPOST.service;

import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.model.Subscriber;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DataGeneratorService {

    private final Random random = new Random();

    public void generateSQLFiles() {
        generateSchemaSQL();
        generateDataSQL();
    }

    private void generateSchemaSQL() {
        String schemaFilePath = "C:/Users/choocha/Downloads/CDRPOST/CDRPOST/src/main/resources/schema.sql"; // Относительный путь
        try (FileWriter writer = new FileWriter(schemaFilePath)) {
            // Создание таблицы subscriber
            writer.write("CREATE TABLE IF NOT EXISTS subscriber (\n");
            writer.write("    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n");
            writer.write("    name VARCHAR(255) NOT NULL,\n");
            writer.write("    phone_number VARCHAR(255) NOT NULL\n");
            writer.write(");\n\n");

            // Создание таблицы cdrcall
            writer.write("CREATE TABLE IF NOT EXISTS cdrcall (\n");
            writer.write("    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n");
            writer.write("    call_type VARCHAR(2) NOT NULL,\n");
            writer.write("    iniciate_number VARCHAR(255) NOT NULL,\n");
            writer.write("    accept_number VARCHAR(255) NOT NULL,\n");
            writer.write("    date_start TIMESTAMP NOT NULL,\n");
            writer.write("    date_end TIMESTAMP NOT NULL\n");
            writer.write(");\n");

            System.out.println("Файл schema.sql успешно создан.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDataSQL() {
        List<Subscriber> subscribers = generateSubscribers();
        List<CDRcall> cdrCalls = generateCDRCalls(subscribers);

        String dataFilePath = "C:/Users/choocha/Downloads/CDRPOST/CDRPOST/src/main/resources/data.sql"; // Относительный путь
        try (FileWriter writer = new FileWriter(dataFilePath))  {
            // Вставка данных в таблицу subscriber
            for (Subscriber subscriber : subscribers) {
                writer.write(String.format(
                        "INSERT INTO subscriber (name, phone_number) VALUES ('%s', '%s');\n",
                        subscriber.getName(), subscriber.getPhoneNumber()
                ));
            }

            // Вставка данных в таблицу cdrcall
            for (CDRcall call : cdrCalls) {
                writer.write(String.format(
                        "INSERT INTO cdrcall (call_type, iniciate_number, accept_number, date_start, date_end) VALUES ('%s', '%s', '%s', '%s', '%s');\n",
                        call.getCall(), call.getIniciate_Number(), call.getAccept_Number(),
                        call.getDateStart(), call.getDateEnd()
                ));
            }

            System.out.println("Файл data.sql успешно создан.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Subscriber> generateSubscribers() {
        List<Subscriber> subscribers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Subscriber subscriber = new Subscriber();
            subscriber.setName("Subscriber " + i);
            subscriber.setPhoneNumber("123456789" + String.format("%02d", i));
            subscribers.add(subscriber);
        }
        return subscribers;
    }

    private List<CDRcall> generateCDRCalls(List<Subscriber> subscribers) {
        List<CDRcall> cdrCalls = new ArrayList<>();
        List<String> randomNumbers = generateRandomNumbers(20);

        LocalDateTime startDate = LocalDateTime.now().minusYears(1);
        LocalDateTime endDate = LocalDateTime.now();

        while (startDate.isBefore(endDate)) {
            int callsPerDay = 20 + random.nextInt(11); // 20-30 звонков в день
            for (int i = 0; i < callsPerDay; i++) {
                CDRcall call = new CDRcall();
                call.setCall(random.nextBoolean() ? "01" : "02");

                if (call.getCall().equals("01")) {
                    Subscriber initiator = subscribers.get(random.nextInt(subscribers.size()));
                    call.setIniciate_Number(initiator.getPhoneNumber());
                    call.setAccept_Number(randomNumbers.get(random.nextInt(randomNumbers.size())));
                } else {
                    call.setIniciate_Number(randomNumbers.get(random.nextInt(randomNumbers.size())));
                    Subscriber acceptor = subscribers.get(random.nextInt(subscribers.size()));
                    call.setAccept_Number(acceptor.getPhoneNumber());
                }

                int callDuration = 1 + random.nextInt(10); // Длительность звонка от 1 до 10 минут
                call.setDateStart(String.valueOf(startDate));
                call.setDateEnd(String.valueOf(startDate.plusMinutes(callDuration)));

                cdrCalls.add(call);
                startDate = startDate.plusMinutes(callDuration);
            }
            startDate = startDate.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        }
        return cdrCalls;
    }

    private List<String> generateRandomNumbers(int count) {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            numbers.add("987654321" + String.format("%02d", i));
        }
        return numbers;
    }
}