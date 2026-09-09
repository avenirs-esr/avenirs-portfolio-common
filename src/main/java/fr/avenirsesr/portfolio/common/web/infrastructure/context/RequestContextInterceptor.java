package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import java.util.concurrent.Callable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;

// RequestContext is a bare ThreadLocal: it never survives the thread hop that async request
// processing (e.g. StreamingResponseBody) introduces. This interceptor bridges that gap by
// capturing the value on the original thread and re-arming it on the worker thread.
class RequestContextInterceptor implements CallableProcessingInterceptor {
  private RequestData capturedContext;

  @Override
  public <T> void beforeConcurrentHandling(NativeWebRequest webRequest, Callable<T> task) {
    capturedContext = RequestContext.get();
  }

  @Override
  public <T> void preProcess(NativeWebRequest webRequest, Callable<T> task) {
    RequestContext.set(capturedContext);
  }

  // Deliberately not postProcess: per Spring's WebAsyncManager, postProcess is skipped on timeout
  // and dispatched differently on error. afterCompletion is the only hook guaranteed to run on
  // every outcome (success, timeout, error), so it's the only safe place to clear.
  @Override
  public <T> void afterCompletion(NativeWebRequest webRequest, Callable<T> task) {
    RequestContext.clear();
  }
}
