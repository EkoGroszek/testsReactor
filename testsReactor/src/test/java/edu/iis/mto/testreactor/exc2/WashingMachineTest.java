package edu.iis.mto.testreactor.exc2;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static edu.iis.mto.testreactor.exc2.LaundryBatch.builder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class WashingMachineTest {

    private WashingMachine washingMachine;

    private DirtDetector dirtDetector;

    private Engine engine;

    private WaterPump waterPump;

    private LaundryBatch laundryBatch;

    private ProgramConfiguration programConfiguration;

    @Before public void init() {

        dirtDetector = mock(DirtDetector.class);
        engine = mock(Engine.class);
        waterPump = mock(WaterPump.class);
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);

    }

    @Test public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test public void weightOverMaxWeightShouldSetErrorCodeAsTOO_HEAVY() {
        LaundryBatch laundryBatch = builder().withWeightKg(10.0)
                                             .withType(Material.DELICATE)
                                             .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration );

        Assert.assertThat(laundryStatus.getErrorCode(), is(ErrorCode.TOO_HEAVY));
    }

    @Test public void weightOverMaxWeightShouldSetResultAsFAILURE() {
        LaundryBatch laundryBatch = builder().withWeightKg(10.0)
                                             .withType(Material.DELICATE)
                                             .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration );

        Assert.assertThat(laundryStatus.getResult(), is(Result.FAILURE));
    }

    @Test public void weightBelowMaxWeightShouldSetResultAsSUCCESS() {
        LaundryBatch laundryBatch = builder().withWeightKg(5.0)
                                             .withType(Material.DELICATE)
                                             .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration );

        Assert.assertThat(laundryStatus.getResult(), is(Result.SUCCESS));
    }

    @Test public void dirtyOver40PercentShouldRunProgramLONG() {
        when(dirtDetector.detectDirtDegree(any())).thenReturn(new Percentage(50.0d));
        LaundryBatch laundryBatch = builder().withWeightKg(5.0)
                                             .withType(Material.DELICATE)
                                             .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.AUTODETECT)
                                                                        .withSpin(true)
                                                                        .build();
        LaundryStatus laundryStatus = washingMachine.start(laundryBatch, programConfiguration );

        Assert.assertThat(laundryStatus.getRunnedProgram(), is(Program.LONG));
    }





}
