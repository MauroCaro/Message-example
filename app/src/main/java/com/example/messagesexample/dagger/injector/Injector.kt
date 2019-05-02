package com.example.messagesexample.dagger.injector

import com.example.messagesexample.dagger.component.MessageComponent

class Injector {
    companion object {
        private lateinit var component: MessageComponent

        fun init(messageComponent: MessageComponent) {
            component = messageComponent
        }

        fun component(): MessageComponent {
            return component
        }
    }
}