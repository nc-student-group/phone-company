-- Returns number of orders of each three most popular product
-- product of each type made in every day between start date and end date
CREATE OR REPLACE FUNCTION get_services_statistics(start_date DATE,
                                                   end_date   DATE)
  RETURNS TABLE(
    order_count     BIGINT,
    s_name          VARCHAR(55),
    s_type          VARCHAR(30),
    s_creation_date DATE
  ) AS
$func$
BEGIN
  RETURN QUERY (SELECT
                  COUNT("order".id) AS order_count,
                  service.service_name,
                  "order".type,
                  "order".creation_date
                FROM "order"
                  INNER JOIN customer_service
                    ON "order".customer_service_id = customer_service.id
                  INNER JOIN service ON customer_service.service_id = service.id
                WHERE (type = 'ACTIVATION' OR type = 'DEACTIVATION')
                      AND "order".creation_date BETWEEN start_date AND end_date
                      AND service.service_name IN (
                  SELECT service_name
                  FROM (
                         SELECT DISTINCT
                           COUNT("order".id)
                           OVER (
                             PARTITION BY service_name ) AS order_number,
                           service_name
                         FROM "order"
                           INNER JOIN customer_service ON "order".customer_service_id = customer_service.id
                           INNER JOIN service ON customer_service.service_id = service.id
                         WHERE type = 'ACTIVATION'
                               AND "order".creation_date BETWEEN start_date AND end_date
                         ORDER BY order_number DESC
                         LIMIT 3) AS most_popular_services)
                GROUP BY service_name, "order".creation_date, type);
END
$func$ LANGUAGE plpgsql;