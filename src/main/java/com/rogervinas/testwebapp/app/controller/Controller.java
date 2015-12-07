package com.rogervinas.testwebapp.app.controller;

import rx.Observer;

import com.rogervinas.testwebapp.server.ServerRequest;

public interface Controller extends Observer<ServerRequest>
{
}
