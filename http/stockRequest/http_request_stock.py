from datetime import datetime, timedelta
import pytz
from apscheduler.schedulers.background import BackgroundScheduler
import redis
import time
import logging

logging.basicConfig()
logging.getLogger('apscheduler').setLevel(logging.DEBUG)

# Redis 연결 설정
redis_client = redis.Redis(host='host.docker.internal', port=6379, db=0)

# Asia/Seoul 시간대 설정
seoul_timezone = pytz.timezone('Asia/Seoul')

# 전역 변수로 scheduler 선언, 시간대를 Asia/Seoul로 설정
scheduler = BackgroundScheduler(timezone=seoul_timezone)

def notify_stock(event_type, item_id):
    # 현재 시간을 Asia/Seoul 기준으로 출력 (단지 로깅 목적)
    current_time = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
    stock_quantity = redis_client.hget(f"item:{item_id}", "stockQuantity").decode('utf-8')
    print(f"[{current_time}] {event_type} - Item ID: {item_id}, Stock Quantity: {stock_quantity}")

def list_initial_stock():
    print("Listing all items and their initial stock quantities:")
    for key in redis_client.scan_iter("item:*"):
        item_id = key.decode().split(":")[1]
        stock_quantity = redis_client.hget(key, "stockQuantity").decode('utf-8')
        print(f"Item ID: {item_id}, Initial Stock Quantity: {stock_quantity}")

def schedule_stock_checks():
    global scheduler
    try:
        scheduler.start()

        for key in redis_client.scan_iter("item:*"):
            item_id = key.decode().split(":")[1]
            item_info = redis_client.hgetall(key)

            if b'reservationStart' in item_info and b'reservationEnd' in item_info:
                reservation_start_str = item_info[b'reservationStart'].decode()
                reservation_end_str = item_info[b'reservationEnd'].decode()

                now = datetime.now(seoul_timezone)
                reservation_start = datetime.strptime(reservation_start_str, '%Y-%m-%dT%H:%M:%S')
                reservation_end = datetime.strptime(reservation_end_str, '%Y-%m-%dT%H:%M:%S')

                if now > reservation_end:
                    logging.warning(f"Reservation end time has already passed for Item ID: {item_id}. Skipping scheduling.")
                    continue

                start_check_time = reservation_start - timedelta(minutes=10)
                end_check_time = reservation_end

                if now < start_check_time:
                    scheduler.add_job(notify_stock, 'date', run_date=start_check_time, args=["Reservation Start", item_id], misfire_grace_time=300)

                scheduler.add_job(notify_stock, 'date', run_date=end_check_time, args=["Reservation End", item_id], misfire_grace_time=300)

    except Exception as e:
        logging.error(f"An error occurred while scheduling stock checks: {e}")

if __name__ == "__main__":
    list_initial_stock()
    schedule_stock_checks()

    try:
        while True:
            time.sleep(1)
    except (KeyboardInterrupt, SystemExit):
        logging.info("Shutting down scheduler...")
        scheduler.shutdown()