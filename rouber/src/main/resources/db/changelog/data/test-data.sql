INSERT INTO public.users(id, name, email, phone_number, address) VALUES
(1, 'Catalin Lungu', 'catalin.lungu@gmail.com', '349763233', 'Str. Stefan cel Mare'),
(2, 'Ioan Pop', 'ioan.pop@gmail.com', '0785156890', 'Str. Mihai Viteazul');


INSERT INTO public.drivers (id, name, email, phone_number, rating) VALUES
(1, 'Mihai Constantin', 'constantin.mihai@gmail.com', 749763232, 0),
(2, 'Tudor Alexandru', 'tudor.alex@gmail.com', 0742763211, 0);


INSERT INTO public.vehicles( id, name, brand, vin, license_plate, color, register_date, created_date, driver_id, state) VALUES
 (1, 'A6 Avant', 'Audi', '0349762232', 'NT52TIS', 'Red', '2022-05-05', '2022-11-10 11:09:21.852128', 1, 'INACTIVE'),
 (2, 'Logan', 'Dacia', 'VZ21WGTS2342152', 'SV52SSS', 'Black', '2021-05-05', '2022-11-10 11:10:01.11313', 1, 'ACTIVE');


INSERT INTO public.trips(id, price, start_lat, start_long, end_lat, end_long, user_id, driver_id, payment_id, rating, start_trip, end_trip) VALUES
(1, 20.00, 111111.1111, 111111.1111, 11111.11111, 111111.11111, 1, null, null, 2, null, null);
