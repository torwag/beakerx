/*
 *  Copyright 2017 TWO SIGMA OPEN SOURCE, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.twosigma.beakerx.cpp;

import com.twosigma.beakerx.KernelSocketsServiceTest;
import com.twosigma.beakerx.cpp.kernel.CppEvaluator;
import com.twosigma.beakerx.cpp.kernel.Cpp;
import com.twosigma.beakerx.kernel.comm.Comm;
import com.twosigma.beakerx.kernel.KernelParameters;
import com.twosigma.beakerx.kernel.KernelRunner;
import com.twosigma.beakerx.message.Message;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.twosigma.MessageAssertions.verifyExecuteReplyMessage;
import static com.twosigma.beakerx.MessageFactoryTest.getExecuteRequestMessage;
import static com.twosigma.beakerx.evaluator.EvaluatorResultTestWatcher.waitForIdleMessage;
import static com.twosigma.beakerx.evaluator.EvaluatorResultTestWatcher.waitForResult;
import static com.twosigma.beakerx.evaluator.EvaluatorResultTestWatcher.waitForSentMessage;
import static com.twosigma.beakerx.evaluator.TestBeakerCellExecutor.cellExecutor;
import static org.assertj.core.api.Assertions.assertThat;

public class CppKernelMainTest {

  private Cpp kernel;

  private KernelSocketsServiceTest kernelSocketsService;

  @Before
  public void setUp() throws Exception {
    String sessionId = "sessionId1";
    CppEvaluator evaluator = new CppEvaluator(sessionId, sessionId, cellExecutor());
    evaluator.setShellOptions(kernelParameters());
    kernelSocketsService = new KernelSocketsServiceTest();
    kernel = new Cpp(sessionId, evaluator, kernelSocketsService);
    new Thread(() -> KernelRunner.run(() -> kernel)).start();
    kernelSocketsService.waitForSockets();
  }

  @After
  public void tearDown() throws Exception {
    kernelSocketsService.shutdown();
  }

  //@Test //disabled because of problem on jenkins
  public void evaluate() throws Exception {
    //given
    String code = "" +
            "// A basic C++ cell\n" +
            "// define a beakerx_main function with an optional parameter for the return value\n" +
            "#include <iostream>\n" +
            "void beakerx_main(){\n" +
            "  std::cout << \"Hello world!\" << std::endl;\n" +
            "  return;\n" +
            "}";

    Message message = getExecuteRequestMessage(code);
    //when
    kernelSocketsService.handleMsg(message);
    //then
    Optional<Message> idleMessage = waitForIdleMessage(kernelSocketsService.getKernelSockets());
    assertThat(idleMessage).isPresent();
    waitForResult(kernelSocketsService.getKernelSockets());
    verifyPublishedMsgs(kernelSocketsService);
    verifyResult(kernelSocketsService.getExecuteResultMessage().get());
    waitForSentMessage(kernelSocketsService.getKernelSockets());
    verifySentMsgs(kernelSocketsService);
  }

  private void verifyPublishedMsgs(KernelSocketsServiceTest service) {
    assertThat(service.getBusyMessage()).isPresent();
    assertThat(service.getExecuteInputMessage()).isPresent();
    assertThat(service.getExecuteResultMessage()).isPresent();
    assertThat(service.getIdleMessage()).isPresent();
  }

  private void verifySentMsgs(KernelSocketsServiceTest service) {
    verifyExecuteReplyMessage(service.getReplyMessage());
  }

  private void verifyResult(Message result) {
    Map actual = ((Map) result.getContent().get(Comm.DATA));
    String value = (String) actual.get("text/plain");
    assertThat(value).isEqualTo("null\nHello world!\n");
  }

  private KernelParameters kernelParameters() {
    Map<String, Object> params = new HashMap<>();
    return new KernelParameters(params);
  }

}