package com.rogervinas.testwebapp.app.route;

import rx.functions.Func1;

import com.rogervinas.testwebapp.server.ServerRequest;

public interface Mapper extends Func1<ServerRequest, ServerRequest>
{
}
