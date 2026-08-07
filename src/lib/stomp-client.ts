"use client";

import { Client, type IMessage, type StompSubscription } from "@stomp/stompjs";

let client: Client | null = null;
let pendingConnectCallbacks: Array<() => void> = [];

function getClient(): Client {
  if (!client) {
    client = new Client({
      brokerURL: process.env.NEXT_PUBLIC_WS_URL ?? "ws://localhost:8080/ws",
      reconnectDelay: 3000,
      onConnect: () => {
        const callbacks = pendingConnectCallbacks;
        pendingConnectCallbacks = [];
        callbacks.forEach((cb) => cb());
      },
    });
    client.activate();
  }
  return client;
}

/**
 * Assina o tópico STOMP do leilão (equivalente ao antigo "auction:join" do
 * Socket.IO — aqui a própria inscrição no tópico já é o "entrar na sala").
 * Retorna uma função de limpeza para desinscrever.
 */
export function subscribeToAuction(
  auctionId: string,
  onEvent: (event: Record<string, unknown>) => void
): () => void {
  const stomp = getClient();
  let subscription: StompSubscription | null = null;

  const doSubscribe = () => {
    subscription = stomp.subscribe(`/topic/auctions/${auctionId}`, (message: IMessage) => {
      onEvent(JSON.parse(message.body));
    });
  };

  if (stomp.connected) {
    doSubscribe();
  } else {
    pendingConnectCallbacks.push(doSubscribe);
  }

  return () => {
    subscription?.unsubscribe();
    pendingConnectCallbacks = pendingConnectCallbacks.filter((cb) => cb !== doSubscribe);
  };
}
