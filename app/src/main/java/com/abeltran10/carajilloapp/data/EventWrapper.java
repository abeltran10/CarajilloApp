package com.abeltran10.carajilloapp.data;

public class EventWrapper <T> {
    private T content;
    private boolean hasBeenHandled = false;

    public EventWrapper(T content) {
        this.content = content;
    }

    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }
}
