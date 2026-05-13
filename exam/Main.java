package exam;

import java.util.HashMap;
import java.util.Map;

abstract class BaseService {
    protected String serviceId;
    public abstract void onUpdate(String data);

    public String getServiceId() {
        return serviceId;
    }
}

class CentralHub {
    private final Map<String, BaseService> services = new HashMap<>();

    public void connectService(BaseService service) {
        services.put(service.getServiceId(), service);
    }

    public void broadcast(String data, String originId) {
        services.values().stream()
                .filter(s -> !s.getServiceId().equals(originId))
                .forEach(s -> s.onUpdate(data));
    }
}

class UserProfile extends BaseService {
    private final CentralHub hub;

    public UserProfile(String id, CentralHub hub) {
        this.serviceId = id;
        this.hub = hub;
    }

    public void createTicket(String info) {
        System.out.println("User [" + serviceId + "] создает тикет: " + info);
        hub.broadcast(info, this.serviceId);
    }

    @Override
    public void onUpdate(String data) {
        System.out.println("Профиль " + serviceId + " уведомлен: " + data);
    }
}

class BillingSystem extends BaseService {
    private final CentralHub hub;

    public BillingSystem(String id, CentralHub hub) {
        this.serviceId = id;
        this.hub = hub;
    }

    @Override
    public void onUpdate(String data) {
        System.out.println("Биллинг [" + serviceId + "] обрабатывает: " + data);
    }
}

public class Main {
    public static void main(String[] args) {
        CentralHub dispatcher = new CentralHub();

        UserProfile user = new UserProfile("User", dispatcher);
        BillingSystem billing = new BillingSystem("System byll", dispatcher);

        dispatcher.connectService(user);
        dispatcher.connectService(billing);

        user.createTicket("Покупка подписки");
    }
}