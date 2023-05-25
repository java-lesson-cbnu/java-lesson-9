package kr.cbnu.lesson9;

import java.util.*;

/**
 * 해당 문제는 네트워크로 CLI 기반 네트워크 게임을 구축하는 예제입니다.
 * <p>
 * 해당 문제는 다음을 충족해야 합니다 :
 * - {@link Game} 클래스를 사용하여 호스트와 클라이언트를 구축하여 네트워크 기반 멀티플레이를 구축해야 합니다.
 * - 각 플레이어는 접속시 UUID를 부여받아야 합니다. 이 UUID는 접속시에 표시되어야 합니다.
 * - 플레이어 2명이 접속시, 2명을 기준으로 게임이 시작되어야 합니다.
 * - 승리한 플레이어에게는 3점, 패배한 플레이어에게는 2점을 주어야 합니다. 이는 {@link SimpleGameNetworkingExample#score} 변수를 사용하여 구현하여야 합니다.
 * - 게임이 끝날 경우, 바로 다음 게임이 진행되어야 합니다.
 * - 게임이 끝나고, 점수가 지급될 경우 {@link SimpleGameNetworkingExample#map}에 순위가 등록되어야 합니다.
 *   예를 들어, A가 20점이고, B가 10점이면 A가 1위 (인덱스 0), B가 2위 (인덱스 1)여야 합니다.
 *   이는 {@link List#sort(Comparator)}를 사용하여 구현할 수 있습니다.
 * - 게임이 끝나고, 순위가 정렬되었을 경우 각 플레이어에게 자신의 순위를 출력해야 합니다.
 * <p>
 * 해당 문제는 다음 조건을 충족시 추가 점수를 받습니다.
 * - 현재는 순위 정렬시, 동기화 문제가 발생할 수 있습니다. 이를 수정하십시오. (4점)
 * - Socket을 대체할 수 있는 프레임워크를 사용합니다. (1점)
 *   다음은 예제이며, 다른 프레임워크를 사용해도 괜찮습니다.
 *   - Netty
 *   - gRPC
 * - 매치메이킹을 구현해야 합니다. 각 플레이어는 게임이 끝나거나, 접속할때 매치메이킹 큐에 진입해야 하며, 게임 도중 혹은 종료될때 매치메이킹 큐에서 제거되어야 합니다.(3점)
 *   - 점수 기반 매치메이킹을 구현합니다. 각 플레이어는 기본으로 30점의 점수 검색 범위를 가져가야 하며, 1초마다 이 범위가 1씩 늘어나야 합니다. (2점)
 * <p>
 */
public class SimpleGameNetworkingExample {

    private final List<UUID> map = new ArrayList<>();

    private final Map<UUID, Integer> score = new HashMap<>();

    public static void main(String[] args) {
        throw new RuntimeException();

    }

    interface PacketSender<T> {
        void sendPacket(T data);
    }


    private static class Game {
        private static final Random random = new Random();

        private int[] playerValues = new int[]{random.nextInt(101), random.nextInt(101)};
        private boolean isFirstPlayerTurn = false;

        public TurnResult doTurn(boolean isFirstPlayerTurn, int value) {
            if (this.isFirstPlayerTurn != isFirstPlayerTurn) {
                return TurnResult.ILLEGAL_TURN;
            }
            this.isFirstPlayerTurn = !this.isFirstPlayerTurn;
            return checkResult(value, playerValues[isFirstPlayerTurn ? 0 : 1]);
        }

        private TurnResult checkResult(int value, int answer) {
            if (value == answer) {
                return TurnResult.WIN;
            }
            if (value < answer) {
                return TurnResult.DOWN;
            }
            return TurnResult.UP;
        }

        static enum TurnResult {
            UP, DOWN, WIN, ILLEGAL_TURN
        }
    }
}
