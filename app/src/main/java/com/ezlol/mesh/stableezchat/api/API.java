package com.ezlol.mesh.stableezchat.api;

import android.content.Context;
import android.util.Log;

import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.model.Attachment;
import com.ezlol.mesh.stableezchat.model.Chat;
import com.ezlol.mesh.stableezchat.model.Event;
import com.ezlol.mesh.stableezchat.model.Message;
import com.ezlol.mesh.stableezchat.model.User;

import com.ezlol.mesh.stableezchat.http.*;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class API {
    public static class AuthException extends Exception {}

    public static final String SERVER_URL = "http://51.210.128.122:8083/api/1.0/";

    public AccessToken accessToken;
    Map<String, String> headers = new HashMap<>();

    private int lastErrorCode;

    public API(String username, String password) throws AuthException {
        accessToken = login(username, password);
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", accessToken.value);
    }

    public API(AccessToken accessToken) {
        this.accessToken = accessToken;
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", accessToken.value);
    }

    public static AccessToken login(String username, String password_hash) throws AuthException {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password_hash);

        Response response = Requests.post(SERVER_URL + "token?grant_type=password", new JSONObject(map).toString());
        if(response == null || response.getStatusCode() / 200 != 1)
            throw new AuthException();
        return new Gson().fromJson(response.toString(), AccessToken.class);
    }

    public User usersGet(int user_id) {
        Response response = Requests.get(SERVER_URL + "users/" + user_id, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1) {
            return new Gson().fromJson(response.toString(), User.class);
        }
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public User[] usersSearch(String username) {
        Response response = Requests.get(SERVER_URL + "users/search/" + username, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1) {
            return new Gson().fromJson(response.toString(), User[].class);
        }
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Chat[] chatsSearch(String title) {
        Response response = Requests.get(SERVER_URL + "messages/chats/search/" + title, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1) {
            return new Gson().fromJson(response.toString(), Chat[].class);
        }
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public boolean editProfile(User user) {
        Response response = Requests.put(SERVER_URL + "users/profile", new Gson().toJson(user, User.class));
        if(response == null)
            return false;
        if(response.getStatusCode() / 200 == 1)
            return true;

        lastErrorCode = response.getStatusCode();
        return false;
    }

    public Chat getChatById(int chat_id) {
        Response response = Requests.get(SERVER_URL + "messages/getChatById/" + chat_id + "?extended=1", headers);
        if(response == null)
            return null;

        Log.d("My", response.toString());
        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Chat.class);

        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Chat createChat(Integer[] user_ids, String title) {
        JSONObject json = new JSONObject();
        try {
            json.put("user_ids", user_ids);
            json.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response response = Requests.post(SERVER_URL + "messages/createChat", json.toString());
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Chat.class);

        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Chat[] getChats() {
        Response response = Requests.get(SERVER_URL + "messages/getChats?extended=1", headers);
        if(response == null)
            return null;

        Log.d("My", response.toString());
        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Chat[].class);

        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Message[] getMessagesByChatId(int chat_id, int count, int offset) {
        Response response = Requests.post(SERVER_URL + "messages/getByChatId/" + chat_id + "?count=" + count + "&offset=" + offset,
                "", headers);
        if(response == null)
            return null;

        Log.d("My", response.toString());
        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Message[].class);

        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Message[] getMessagesByChatId(int chat_id, int count) {
        return getMessagesByChatId(chat_id, count, 0);
    }

    public Message[] getMessagesByChatId(int chat_id) {
        return getMessagesByChatId(chat_id, 20, 0);
    }

    public Message getMessageById(int message_id) {
        Message[] messages = getMessagesById(String.valueOf(message_id));
        if(messages == null || messages.length == 0)
            return null;
        return messages[0];
    }

    public Message[] getMessagesById(Integer[] message_ids) {
        StringBuilder s = new StringBuilder();
        for(int id : message_ids) {
            s.append(id).append(",");
        }
        return getMessagesById(s.substring(0, s.length() - 1));
    }

    public Message[] getMessagesById(String message_ids) {
        Response response = Requests.get(SERVER_URL + "messages/getById/" + message_ids, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Message[].class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Message sendMessage(Message message) {
        Response response = Requests.post(SERVER_URL + "messages/send", new Gson().toJson(message, Message.class), headers);
        if(response == null)
            return null;

        Log.d("sendMessage", response.toString());

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Message.class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Message messagesSend(int chat_id, String content, String attachment) {
        JSONObject json = new JSONObject();
        try {
            json.put("chat_id", chat_id);
            json.put("content", content);
            json.put("attachment", attachment);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response response = Requests.post(SERVER_URL + "messages/send", json.toString(), headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Message.class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Message messagesRead(int message_id) {
        Response response = Requests.post(SERVER_URL + "messages/markAsRead/" + message_id, "", headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Message.class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public void messagesSetActivity(int chat_id, String type) {
        JSONObject json = new JSONObject();
        try {
            json.put("chat_id", chat_id);
            json.put("type", type);
        } catch (JSONException ignored) {
            Log.wtf("API.messagesSetActivity", "WTF?!");
            return;
        }

        Response response = Requests.post(SERVER_URL + "messages/setActivity",
                json.toString(),
                headers);
        if(response == null)
            return;

        if(response.getStatusCode() / 200 == 1)
            return;
        lastErrorCode = response.getStatusCode();
    }

    public Attachment uploadAttachment(File file) {
        String mimeType;
        try {
            mimeType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            mimeType = "text/plain";
        }
        Response response = Requests.uploadFile(SERVER_URL + "attachments/upload", file, mimeType, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Attachment.class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public Attachment getAttachment(int attachment_id) {
        Response response = Requests.get(SERVER_URL + "attachments/get/" + attachment_id, headers);
        if(response == null)
            return null;

        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Attachment.class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public File downloadAttachmentData(Context context, Attachment attachment) throws IOException {
        File downloadedFile = new File(context.getCacheDir(), attachment.hash);
        if(downloadedFile.exists())
            return downloadedFile;

        try (BufferedInputStream in = new BufferedInputStream(new URL(SERVER_URL + attachment.url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(downloadedFile)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return downloadedFile;
    }

    public Event[] events() {
        Response response = Requests.get(SERVER_URL + "event", headers);
        if(response == null)
            return null;

        Log.d("Content", response.toString());
        if(response.getStatusCode() / 200 == 1)
            return new Gson().fromJson(response.toString(), Event[].class);
        lastErrorCode = response.getStatusCode();
        return null;
    }

    public int getLastErrorCode() {
        return lastErrorCode;
    }
}
