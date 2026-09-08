package fr.avenirsesr.portfolio.common.web.infrastructure.context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import fr.avenirsesr.portfolio.common.language.domain.model.enums.ELanguage;
import fr.avenirsesr.portfolio.common.testutils.BddLogger;
import java.util.Optional;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.NativeWebRequest;

class RequestContextInterceptorTest {

  private final RequestContextInterceptor interceptor = new RequestContextInterceptor();
  private final NativeWebRequest nativeWebRequest = mock(NativeWebRequest.class);

  @SuppressWarnings("unchecked")
  private final Callable<Object> task = mock(Callable.class);

  @BeforeEach
  void setup() {
    RequestContext.clear();
  }

  @AfterEach
  void cleanup() {
    RequestContext.clear();
  }

  @Nested
  class GivenARequestContextInterceptor {

    @BeforeEach
    void setupGiven() {
      BddLogger.given("a RequestContextInterceptor");
    }

    @Nested
    class WhenTheOriginalThreadHasAContext {

      RequestData originalData;

      @BeforeEach
      void setupWhen() {
        BddLogger.when("beforeConcurrentHandling is called while a context is set");
        originalData = new RequestData(Optional.empty(), ELanguage.ENGLISH);
        RequestContext.set(originalData);
      }

      @Test
      void thenPreProcessShouldRestoreTheCapturedContextOnAnotherThread() throws Exception {
        BddLogger.then("preProcess should make the same context visible on a different thread");

        interceptor.beforeConcurrentHandling(nativeWebRequest, task);
        RequestData[] seenOnAsyncThread = new RequestData[1];

        Thread asyncThread =
            new Thread(
                () -> {
                  try {
                    interceptor.preProcess(nativeWebRequest, task);
                    seenOnAsyncThread[0] = RequestContext.get();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
        asyncThread.start();
        asyncThread.join();

        assertEquals(originalData, seenOnAsyncThread[0]);
      }

      @Test
      void thenAfterCompletionShouldClearTheContextOnTheAsyncThread() throws Exception {
        BddLogger.then(
            "afterCompletion should clear the context on the thread that ran preProcess");

        interceptor.beforeConcurrentHandling(nativeWebRequest, task);
        RequestData[] afterClear = new RequestData[1];

        Thread asyncThread =
            new Thread(
                () -> {
                  try {
                    interceptor.preProcess(nativeWebRequest, task);
                    interceptor.afterCompletion(nativeWebRequest, task);
                    afterClear[0] = RequestContext.get();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
        asyncThread.start();
        asyncThread.join();

        assertNull(afterClear[0]);
      }

      @Test
      void thenTheOriginalThreadShouldBeUnaffectedByTheAsyncThreadsLifecycle() throws Exception {
        BddLogger.then("the original thread's own context should remain untouched");

        interceptor.beforeConcurrentHandling(nativeWebRequest, task);

        Thread asyncThread =
            new Thread(
                () -> {
                  try {
                    interceptor.preProcess(nativeWebRequest, task);
                    interceptor.afterCompletion(nativeWebRequest, task);
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
        asyncThread.start();
        asyncThread.join();

        assertEquals(originalData, RequestContext.get());
      }
    }

    @Nested
    class WhenTheOriginalThreadHasNoContext {

      @BeforeEach
      void setupWhen() {
        BddLogger.when("beforeConcurrentHandling is called with no context set");
      }

      @Test
      void thenPreProcessShouldLeaveTheAsyncThreadWithoutContextRatherThanThrowing()
          throws Exception {
        BddLogger.then("preProcess should not throw and should leave the context null");

        interceptor.beforeConcurrentHandling(nativeWebRequest, task);
        RequestData[] seenOnAsyncThread =
            new RequestData[] {new RequestData(Optional.empty(), ELanguage.FRENCH)};

        Thread asyncThread =
            new Thread(
                () -> {
                  try {
                    interceptor.preProcess(nativeWebRequest, task);
                    seenOnAsyncThread[0] = RequestContext.get();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
        asyncThread.start();
        asyncThread.join();

        assertNull(seenOnAsyncThread[0]);
      }
    }

    @Nested
    class WhenTheAsyncTaskDoesNotCompleteNormally {

      @BeforeEach
      void setupWhen() {
        BddLogger.when(
            "the callable times out or errors instead of completing (Spring still guarantees"
                + " afterCompletion runs via addCompletionHandler, regardless of outcome)");
        RequestContext.set(new RequestData(Optional.empty(), ELanguage.SPANISH));
      }

      @Test
      void thenAfterCompletionShouldStillClearTheContextOnTheAsyncThread() throws Exception {
        BddLogger.then(
            "the context should be cleared even though postProcess is never reached on this"
                + " path — this is exactly the case afterCompletion was introduced to cover");

        interceptor.beforeConcurrentHandling(nativeWebRequest, task);
        RequestData[] afterAbnormalCompletion = new RequestData[1];

        Thread asyncThread =
            new Thread(
                () -> {
                  try {
                    interceptor.preProcess(nativeWebRequest, task);
                    interceptor.afterCompletion(nativeWebRequest, task);
                    afterAbnormalCompletion[0] = RequestContext.get();
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
        asyncThread.start();
        asyncThread.join();

        assertNull(
            afterAbnormalCompletion[0],
            "afterCompletion doit nettoyer le contexte même sur un chemin timeout/erreur");
      }
    }
  }
}
