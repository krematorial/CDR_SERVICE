package kulagoSA.CDRPOST.service;
import kulagoSA.CDRPOST.model.CDRcall;
import kulagoSA.CDRPOST.model.Subscriber;
import kulagoSA.CDRPOST.repository.InMemoryCDRcallDAO;
import kulagoSA.CDRPOST.repository.InMemorySubcriberDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

    @Service
    public class DataGeneratorService {

        @Autowired
        private InMemorySubcriberDAO subscriberRepository;
        @Autowired
        private InMemoryCDRcallDAO CDRCallRepository;

        private final Random random = new Random();

        public void generateData() {
            generateSubscribers();
            generateCDRCalls();
        }

        private void generateSubscribers() {
            List<Subscriber> subscribers = new ArrayList<>();
            for (int i = 1; i <= 15; i++) {
                Subscriber subscriber = new Subscriber();
                subscriber.setName("Subscriber " + i);
                subscriber.setPhoneNumber("123456789" + String.format("%02d", i));
                subscribers.add(subscriber);
            }
            subscriberRepository.saveSUB((Subscriber) subscribers);
        }

        private void generateCDRCalls() {
            List<Subscriber> subscribers = (List<Subscriber>) subscriberRepository.findAllSUBData();
            List<String> randomNumbers = generateRandomNumbers(20);

            LocalDateTime startDate = LocalDateTime.now().minusYears(1);
            LocalDateTime endDate = LocalDateTime.now();

            while (startDate.isBefore(endDate)) {
                int callsPerDay = 20 + random.nextInt(11); // 20-30 звонков в день
                for (int i = 0; i < callsPerDay; i++) {
                    CDRcall call = new CDRcall();
                    call.setCall(random.nextBoolean() ? "01" : "02");

                    if (call.getCall().equals("01")) {
                        call.setIniciate_Number(subscribers.get(random.nextInt(subscribers.size())).getPhoneNumber());
                        call.setAccept_Number(randomNumbers.get(random.nextInt(randomNumbers.size())));
                    } else {
                        call.setIniciate_Number(randomNumbers.get(random.nextInt(randomNumbers.size())));
                        call.setAccept_Number(subscribers.get(random.nextInt(subscribers.size())).getPhoneNumber());
                    }

                    int callDuration = 1 + random.nextInt(10); // Длительность звонка от 1 до 10 минут
                    call.setDateStart(String.valueOf(startDate));
                    call.setDateEnd(String.valueOf(startDate.plusMinutes(callDuration)));

                    CDRCallRepository.saveCDR(call);
                    startDate = startDate.plusMinutes(callDuration);
                }
                startDate = startDate.plusDays(1).withHour(0).withMinute(0).withSecond(0);
            }
        }

        private List<String> generateRandomNumbers(int count) {
            List<String> numbers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                numbers.add("987654321" + String.format("%02d", i));
            }
            return numbers;
        }
    }
