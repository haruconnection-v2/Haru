import { useRef } from 'react';

const THROTTLE_DEFAULT_TIME = 1000; // 기본값: 1초

const useThrottle = (callback, throttleTime = THROTTLE_DEFAULT_TIME) => {
  const timer = useRef(null);

  return (...args) => {
    if (timer.current) return;

    callback(...args);
    timer.current = setTimeout(() => {
      timer.current = null;
    }, throttleTime);
  }
}
export default useThrottle;