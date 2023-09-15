package test;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        String json = "..."; // Ваш JSON строка

        Gson gson = new Gson();
        AccountInfo accountInfo = gson.fromJson(json, AccountInfo.class);

        // Далее вы можете работать с объектом `accountInfo` как обычно.
    }
}

